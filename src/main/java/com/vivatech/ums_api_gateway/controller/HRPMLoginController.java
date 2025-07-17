package com.vivatech.ums_api_gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivatech.ums_api_gateway.dto.*;
import com.vivatech.ums_api_gateway.helper.UMSEnums;
import com.vivatech.ums_api_gateway.helper.Utility;
import com.vivatech.ums_api_gateway.login.CustomUserDetailsService;
import com.vivatech.ums_api_gateway.login.JwtTokenUtil;
import com.vivatech.ums_api_gateway.model.*;
import com.vivatech.ums_api_gateway.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.synchronoss.cloud.nio.multipart.MultipartUtils.getHeaders;

@RestController
@RequestMapping(path = "/hrpm/users")
public class HRPMLoginController extends BaseController {

  private static final Logger logger = LoggerFactory.getLogger(HRPMLoginController.class);

  @Value("${core-server-address}")
  private String coreServerAddress;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private CustomUserDetailsService service;

  @Value("${token.expiry}")
  private Integer expiryTime;

  @Value("${login.otp.fix.password}")
  private Boolean loginOTPStatus;

  @Autowired
  private PrivilegeRoleRepository privilegeRoleRepository;

  @Autowired
  private PrivilegeRepository privilegeRepository;
  @Autowired
  private NewUsersRepository newUsersRepository;
  @Autowired
  private InstructorsRepository instructorsRepository;
  @Autowired
  private StudentRepository studentRepository;
  @Autowired
  private PasswordEncoder bcryptEncoder;
  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private UserRolesRepository userRolesRepository;


  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public AuthRequestDto generateToken(@RequestBody AuthRequestDto authRequestDto) throws Exception {
    logger.info("Entering generateToken");
    String msisdn = null;
    try {
      Student student = studentRepository.findByRegistrationNo(authRequestDto.getUsername());
      if (student != null && student.getStatus().equalsIgnoreCase(Utility.convertEnumToString(String.valueOf(UMSEnums.StudentStatus.IN_ACTIVE)))) {
        authRequestDto.setErrorMessage("User not found");
        return authRequestDto;
      }
      if (student != null && student.getStatus().equalsIgnoreCase("ACTIVE")) {
        authRequestDto.setFullname(student.getFirstName());
        msisdn = student.getContactNo();
      }

      Instructors instructors = instructorsRepository.findByCode(authRequestDto.getUsername());
      if (instructors != null && instructors.getStatus().equalsIgnoreCase("INACTIVE")) {
        authRequestDto.setErrorMessage("User not found");
        return authRequestDto;
      }

      if (instructors != null && instructors.getStatus().equalsIgnoreCase("ACTIVE")) {
        authRequestDto.setFullname(instructors.getName());
        msisdn = instructors.getContactnumber();
      }
      UserDetails userDetails = service.loadUserByUsername(authRequestDto.getUsername());
      if (userDetails == null) {
        authRequestDto.setErrorMessage("Invalid User name.");
        return authRequestDto;
      }

      NewUsers newUsers = newUsersRepository.findByUserName(userDetails.getUsername());
      if (newUsers.getStatus() != null && newUsers.getStatus().equalsIgnoreCase("Blocked")) {
        logger.info("User: {} is blocked", userDetails.getUsername());
        authRequestDto.setErrorMessage("You are blocked, please try again later");
        return authRequestDto;
      }


      if (!passwordEncoder.matches(authRequestDto.getPassword(), userDetails.getPassword())) {
        if (newUsers.getUserRoles().getId() == 1) {
          authRequestDto.setErrorMessage("Wrong Password!");
        } else {
          if (newUsers.getLoginAttempt() == null) {
            newUsers.setLoginAttempt(1);
          } else {
            newUsers.setLoginAttempt(newUsers.getLoginAttempt() + 1);
          }

          if (newUsers.getLoginAttempt() > 2) {
            newUsers.setStatus("Blocked");
            newUsers.setBlockedTime(new Date());
          }
          logger.info("Wrong password entered by: " + userDetails.getUsername());
          authRequestDto.setErrorMessage(String.format("Wrong Password %d attempt is left", 3 - newUsers.getLoginAttempt()));

          newUsersRepository.save(newUsers);
        }
        logger.info("Wrong password entered by: {}", authRequestDto.getUsername());
        return authRequestDto;
      }

      if (authRequestDto.getOtp() == null) {
        // send otp to user;
        sendOtpToUser(msisdn, newUsers);
      }


      if (newUsers.getFirstlogin() == null) {
        newUsers.setFirstlogin(0);
      }

      newUsers.setStatus(null);
      newUsers.setLoginAttempt(null);
      newUsersRepository.save(newUsers);

      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
//        System.out.println("Expiry Time: "+ expiryTime);
      cal.add(Calendar.MINUTE, expiryTime);
      String token = jwtTokenUtil.generateToken(authRequestDto.getUsername(), expiryTime);
      UserRoles role = service.findRolesByUsername(authRequestDto.getUsername());
      List<PrivilegeRoles> rolePrivilegeList = privilegeRoleRepository.findByRoleId(role.getId());
      List<SubPrivilegeDto> privilegeList = new ArrayList<>();

      for (PrivilegeRoles p : rolePrivilegeList) {
        Privilege privilege = privilegeRepository.findById(p.getPrivilegeId()).orElseThrow(() -> new RuntimeException(String.format("Privilege ID: %d not found", p.getPrivilegeId())));
        SubPrivilegeDto dto = new SubPrivilegeDto();
        dto.setId(privilege.getId());
        dto.setName(privilege.getName());
        dto.setDisplayName(privilege.getDisplayName());
        dto.setSubPrivilegeId(p.getSubPrivilegeId());
        privilegeList.add(dto);
      }
      authRequestDto.setTokenExpiry(cal.getTime());

      authRequestDto.setRoles(role);
      authRequestDto.setPrivilegeList(privilegeList);
      authRequestDto.setPassword(authRequestDto.getPassword());
      authRequestDto.setFirstlogin(newUsers.getFirstlogin());
      authRequestDto.setAuthId(newUsers.getId());

      if (authRequestDto.getOtp() != null) {
        return verifyOtp(authRequestDto, newUsers, token);
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.info("Exception in generateToken: " + e.getMessage());
    } finally {
      logger.info("Logged in user: {}", authRequestDto);
      logger.info("Exiting generateToken");
    }

    return authRequestDto;
  }

  private AuthRequestDto verifyOtp(AuthRequestDto authRequestDto, NewUsers newUsers, String token) {
    logger.info("Entering verifyOtp");
    try {
      if (!loginOTPStatus) {
        NewUsers newUser = newUsersRepository.findByUserName(authRequestDto.getUsername());
        if (newUsers == null) {
          authRequestDto.setResult("Failed");
          authRequestDto.setErrorMessage("User not found");
          return authRequestDto;
        }

        if (authRequestDto.getOtp().equals(newUser.getOtp())) {
          authRequestDto.setToken(token);
          authRequestDto.setResult("success");
          return authRequestDto;
        } else {
          authRequestDto.setResult("Failed");
          authRequestDto.setErrorMessage("Incorrect OTP entered, please enter the correct OTP sent to your registered mobile number.");
          return authRequestDto;
        }
      } else {
        if (authRequestDto.getOtp() == 123456) {
          authRequestDto.setToken(token);
          authRequestDto.setResult("success");
          return authRequestDto;
        } else {
          authRequestDto.setResult("Failed");
          authRequestDto.setErrorMessage("Wrong OTP!");
          return authRequestDto;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.info("Exception in verifyOtp: " + e.getMessage());
    } finally {
      logger.info("Exiting verifyOtp");
    }
    return authRequestDto;
  }

  private void sendOtpToUser(String msisdn, NewUsers newUsers) throws IOException {
    if (newUsers != null) {
      if (!loginOTPStatus) {
        int randomPIN = (int) (Math.random() * 900000) + 100000;
        newUsers.setOtp(randomPIN);
        newUsersRepository.save(newUsers);
        String message = "Your otp is " + randomPIN;
//         TODO: Notification
//        smsService.sendMessageToUsers(msisdn, message);
      }

    }
  }

  @Autowired
  private RestTemplate restTemplate;

  @RequestMapping(value = "/find-by-username", method = RequestMethod.POST)
  public ResponseEntity<Object> findByUsername(@RequestBody String username) {
    String url = coreServerAddress + "users/find-by-username";
    return new ResponseEntity<>(restTemplate.postForObject(url, username, Object.class), HttpStatus.OK);
  }

  @PostMapping(path = "/update-password") // Map ONLY POST Requests
  public Response updatePassword(@RequestBody ChangePasswordDto dto) throws JsonProcessingException {
    logger.info("Entering updatePassword");
    Response response = new Response();
    ObjectMapper obj = new ObjectMapper();
    try {
      logger.info("REQUEST : " + obj.writeValueAsString(dto));
      NewUsers newUsers = newUsersRepository.findByUserName(dto.getUserName());
      if (newUsers == null) {
        response.setResult("Failed");
        response.setMessage("User not found");
        logger.info(response.getMessage());
        return response;
      }
      if (!passwordEncoder.matches(dto.getOldpassword(), newUsers.getPassword())) {
        response.setResult("Failed");
        response.setMessage("Password does not match");
        logger.info("Password does not match for user {}", newUsers.getUserName());
        return response;
      }
      if (passwordEncoder.matches(dto.getPassword(), newUsers.getPassword())) {
        response.setResult("Failed");
        response.setMessage("Please do not use last used password!");
        logger.info("Do not use old password {}", newUsers.getUserName());
        return response;
      }
      newUsers.setPassword(bcryptEncoder.encode(dto.getPassword()));
      newUsers.setFirstlogin(1);
      newUsersRepository.save(newUsers);
      if (newUsers.getUserRoles().getName().equalsIgnoreCase("STUDENT")) {
        smsNotificationToStudent(newUsers.getUserName());
      }
      response.setResult("Success");
      response.setMessage("Password changed successfully");
      logger.info(response.getMessage());
    } catch (Exception e) {
      // TODO: handle exception
      return new Response("", e.getMessage());
    } finally {
      logger.info("RESPONSE : " + obj.writeValueAsString(response));
      logger.info("Exiting updatePassword");
    }
    return response;
  }

  // verify username and email for forget password
  @PostMapping(value = "/check-email-username")
  public AuthRequestDto verifyEmailAndUsername(@RequestBody NewUsers in) throws JsonProcessingException {
    logger.info("Entering verifyEmailAndUsername");
    AuthRequestDto dto = new AuthRequestDto();
    ObjectMapper obj = new ObjectMapper();
    try {
      logger.info("REQUEST : " + obj.writeValueAsString(in));
      Student student = studentRepository.findByRegistrationNo(in.getUserName());
      if (student != null && student.getStatus().equalsIgnoreCase(Utility.convertEnumToString(String.valueOf(UMSEnums.StudentStatus.IN_ACTIVE)))) {
        dto.setErrorMessage("User not found");
        return dto;
      }
      Instructors instructors = instructorsRepository.findByCode(in.getUserName());
      if (instructors != null && instructors.getStatus().equalsIgnoreCase("IN ACTIVE")) {
        dto.setErrorMessage("User not found");
        return dto;
      }
      NewUsers userData = newUsersRepository.findByUserName(in.getUserName());
      if (userData != null && userData.getEmail().equalsIgnoreCase(in.getEmail())) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, expiryTime);
        String token = jwtTokenUtil.generateToken(in.getUserName(), expiryTime);
        dto.setUsername(in.getUserName());
        dto.setToken(token);
        dto.setTokenExpiry(cal.getTime());
        dto.setRoles(userData.getUserRoles());
      } else {
        dto.setErrorMessage("Cannot find username or email");
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.info("Exception in verifyEmailAndUsername: " + e.getMessage());
    } finally {
      logger.info("RESPONSE : " + obj.writeValueAsString(dto));
      logger.info("Exiting verifyEmailAndUsername");
    }
    return dto;
  }

  @PostMapping(value = "/forget-password")
  public Response forgetPassword(@RequestBody ChangePasswordDto dto) throws JsonProcessingException {
    logger.info("Entering forgetPassword");
    Response response = new Response();
    ObjectMapper obj = new ObjectMapper();
    try {
      logger.info("REQUEST : " + obj.writeValueAsString(dto));
      NewUsers newUsers = newUsersRepository.findByUserName(dto.getUserName());
      if (newUsers == null) {
        response.setResult("Failed");
        response.setMessage("User not found");
        logger.info(response.getMessage());
        return response;
      }
      newUsers.setPassword(bcryptEncoder.encode(dto.getPassword()));
      newUsersRepository.save(newUsers);
      if (newUsers.getUserRoles().getName().equalsIgnoreCase("STUDENT")) {
        smsNotificationToStudent(newUsers.getUserName());
      }
      if (!newUsers.getUserRoles().getName().equalsIgnoreCase("STUDENT")) {
        smsNotificationToStaff(newUsers.getUserName());
      }
      response.setResult("Success");
      response.setMessage("Password changed successfully");
      logger.info(response.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      logger.info("Exception in forgetPassword: " + e.getMessage());
    } finally {
      logger.info("RESPONSE : " + obj.writeValueAsString(response));
      logger.info("Exiting forgetPassword");
    }
    return response;
  }

  // user details find by username for change password.
  @RequestMapping(value = "/find-all", method = RequestMethod.POST)
  public NewUsers findByUserDetails(@RequestBody String username) {
    return newUsersRepository.findByUserName(username);
  }

  private void smsNotificationToStaff(String userName) {
    logger.info("Entering update password to send mail ");
    Instructors instructors = instructorsRepository.findByCode(userName);
    if (instructors != null) {
      String mobileNo = instructors.getContactnumber();
      String sms = "Your Login Password has been changed successfully!";
      logger.info("username: {}, mobile No: {}, message: {}", userName, mobileNo, sms);
//      TODO: Notification
//      notificationService.saveSMSNotification(mobileNo, sms, "");
    } else {
      logger.info("User name: {}, not found in staff list so cannot send sms", userName);
    }
    logger.info("Exiting update password to send mail");
  }

  private void smsNotificationToStudent(String userName) {
    logger.info("Entering update password to send mail ");
    Student studentData = studentRepository.findByRegistrationNo(userName);
    String mobileNo = studentData.getContactNo();
    String sms = "Your Login Password has been changed successfully!";
    logger.info("username: {}, mobile No: {}, message: {}", userName, mobileNo, sms);

//    TODO: Notification
//    notificationService.saveSMSNotification(mobileNo, sms, "");
    logger.info("Exiting update password to send mail");

  }

  @RequestMapping(value = "/find-by-block-user", method = RequestMethod.GET)
  public ResponseEntity<Object> findByBlockUser() {

    String url = coreServerAddress + "users/find-by-block-user";

    return getGETApiResponse(restTemplate, url, Object.class);
  }

  @RequestMapping(value = "/find-by-block-user", method = RequestMethod.POST)
  public ResponseEntity<Object> filterByBlockUser(@RequestBody BlockUserDto b) {

    String url = coreServerAddress + "users/find-by-block-user";

    return getGETApiResponse(restTemplate, url, Object.class);
  }

  @RequestMapping(value = "/unblocked-user", method = RequestMethod.GET)
  public ResponseEntity<Object> approvalUnblockUser(@RequestParam Integer id) {
    String url = coreServerAddress + "users/unblocked-user?id=" + id;

    return getGETApiResponse(restTemplate, url, Object.class);
  }

  @GetMapping(value = "/graduation/type")
  public String getGradType(){
    String url = "http://viva-telecom.abto.lan/dev/hrpm/graduation/type";
    String template = restTemplate.getForObject(url, String.class);
    logger.info("Response {}", template);
    return template;
  }

  @PostMapping(value = "/report/training")
  public ResponseEntity<String> getTrainingReport(@RequestParam String startDate, @RequestParam String endDate, @RequestBody ReportDto reportDto){
    Map<String, Object> hashData = reportDto.getHashData();

    StringBuilder urlBuilder = new StringBuilder("http://139.84.167.86:7070/hrpm/report/training?startDate=" + startDate + "&endDate=" + endDate);

    if(hashData != null) {
      for (Map.Entry<String, Object> entry : hashData.entrySet()) {
        urlBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
      }
    }

    String url = urlBuilder.toString();

    //String url = "http://139.84.167.86:7070/hrpm/report/training?startDate=" + startDate + "&endDate=" + endDate;
    HttpHeaders headers = new HttpHeaders();
    headers.add("user-id", reportDto.getUserId().toString()); // Replace with the actual user ID value

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);

    // Encode the binary data to Base64
    byte[] binaryData = response.getBody();
    if (binaryData == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Failed to retrieve the training report.");
    }

    String base64EncodedData = Base64.getEncoder().encodeToString(binaryData);

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.TEXT_PLAIN); // Plain text for Base64 encoded data

    return new ResponseEntity<>(base64EncodedData, responseHeaders, response.getStatusCode());
  }


}
