package com.vivatech.ums_api_gateway.login;


import com.vivatech.ums_api_gateway.helper.Constants;
import com.vivatech.ums_api_gateway.model.Privilege;
import com.vivatech.ums_api_gateway.model.PrivilegeRoles;
import com.vivatech.ums_api_gateway.model.UserRoles;
import com.vivatech.ums_api_gateway.repository.PrivilegeRepository;
import com.vivatech.ums_api_gateway.repository.PrivilegeRoleRepository;
import com.vivatech.ums_api_gateway.repository.UserRolesRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtRequestFilter implements WebFilter {

  @Autowired
  private JwtTokenUtil jwtUtil;

  @Autowired
  private CustomUserDetailsService service;

  @Autowired
  private UserRolesRepository userRolesRepository;

  @Autowired
  private PrivilegeRoleRepository privilegeRoleRepository;

  @Autowired
  private PrivilegeRepository privilegeRepository;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    try {
      if (skipURL(exchange.getRequest().getURI().getPath())) {
        return chain.filter(exchange);
      }

      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        String token = authorizationHeader.substring(7);
        if (!isValid(token)) {
          return Mono.defer(() -> {
            exchange.getResponse().getHeaders().add("UMS Authentication", "Exception");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
          });
        }

        String username = jwtUtil.extractUserName(token);
        Claims claims = jwtUtil.extractAllClaims(token);
        List<String> roles = (List<String>) claims.get(Constants.CLAIM_ROLE);

        UserDetails userDetails = service.loadUserByUsername(username); // Blocking call

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, getGrantedAuthorities(roles));
        /*SecurityContext context = new SecurityContextImpl(authenticationToken);
        return chain.filter(exchange)
                .subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)))
                .onErrorResume(e -> {
                  exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                  return exchange.getResponse().setComplete();
                });*/

        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.just(new SecurityContextImpl()))
                .doOnNext(context -> context.setAuthentication(authenticationToken))
                .then(chain.filter(exchange));

      }
    } catch (Exception e){
      e.printStackTrace();
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
    return chain.filter(exchange);
  }

  private boolean isValid(String token) {
    boolean validation = false;
    try {
      Jwts.parser().setSigningKey("university").parseClaimsJws(token).getBody().getSubject();
      validation = true;
    } catch (ExpiredJwtException e) {
      System.out.println(" Token expired ");
      validation = false;
    } catch(Exception e){
      System.out.println(" Token might be expired ");
    }
    return validation;
  }

  private boolean skipURL(String requestURI){
    List<String> skipURL = Arrays.asList("/core/users/authenticate", "/core/faculty/**", "/core/departments/**", "/core/shifts/**", "/core/semesters/**"
            , "/core/semco/**", "/core/student/online-application-status", "/core/users/update-password", "/hrpm/users/authenticate");
    return skipURL.stream().anyMatch(ele -> ele.contains(requestURI));
  }

  private List<GrantedAuthority> getGrantedAuthorities(final List<String> roles) {
    final List<GrantedAuthority> authorities = new ArrayList<>();
    if (roles != null) {
      for (String role : roles){
        authorities.add(new SimpleGrantedAuthority(role));
        UserRoles userRole = userRolesRepository.findByName(role);
        List<PrivilegeRoles> privilegeRoles = privilegeRoleRepository.findByRoleId(userRole.getId());
        List<Integer> privilegeIds = privilegeRoles.stream().map(PrivilegeRoles::getPrivilegeId).collect(Collectors.toList());
        List<Privilege> privilegeList = role.equalsIgnoreCase("ADMIN") ? privilegeRepository.findAll() : privilegeRepository.findAllById(privilegeIds);
        List<String> privileges = privilegeList.stream().map(Privilege::getDisplayName).collect(Collectors.toList());
        if (!privileges.isEmpty()) {
          for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
          }
        }
      }
    }

    return authorities;
  }


}
