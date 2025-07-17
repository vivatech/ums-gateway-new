package com.vivatech.ums_api_gateway.login;

import com.vivatech.ums_api_gateway.helper.Constants;
import com.vivatech.ums_api_gateway.model.NewUsers;
import com.vivatech.ums_api_gateway.model.UserRoles;
import com.vivatech.ums_api_gateway.repository.NewUsersRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	@Value("${jwt.secret}")
	private String secret;

    @Autowired
    private NewUsersRepository newUsersRepository;

	// retrieve username from jwt token
  public String generateToken(String username, Integer tokenExpiry){
    Map<String, Object> claims = new HashMap<String, Object>();
    return createToken(claims, username, tokenExpiry);
  }

  private String createToken(Map<String, Object> claims, String username, Integer tokenExpiry){
    NewUsers user = newUsersRepository.findByUserName(username);
    return Jwts.builder().setClaims(claims)
      .setSubject(username)
      .claim(Constants.CLAIM_ROLE, rolesAsString(Collections.singleton(user.getUserRoles())))
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * tokenExpiry))
      .signWith(SignatureAlgorithm.HS256,secret).compact();
  }

  public boolean validateToken(String token, UserDetails userDetails){
    final String username = extractUserName(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private List<String> rolesAsString(Collection<UserRoles> userRoles) {
    List<String> rolesAndPermissions = new ArrayList<>();
    for (UserRoles role : userRoles) {
      addIfNotPresent(rolesAndPermissions, role.getName());
    }
    return  rolesAndPermissions;
  }

  private List<String> addIfNotPresent(List<String> rolesAndPermissions, String item) {
    if (!rolesAndPermissions.contains(item)) {
      rolesAndPermissions.add(item);
    }
    return rolesAndPermissions;
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new  Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }
}
