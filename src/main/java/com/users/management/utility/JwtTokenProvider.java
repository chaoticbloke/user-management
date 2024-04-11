package com.users.management.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.users.management.domain.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class JwtTokenProvider {

    //usually this will be stored in secure server.
    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(UserPrincipal userPrincipal) {
        String [] claims  = getClaimsFromUser(userPrincipal);

        // Adding one day to current date
        Date futureDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

        return JWT.create().withIssuer("TEST_COMPANY_NAME").withAudience("TEST_COMPANY_ADMINISTRATION")
                .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
                .withArrayClaim(String.valueOf(userPrincipal.getAuthorities()),claims)
                .withExpiresAt(futureDate).sign(Algorithm.HMAC512(secret.getBytes()));
    }
    public List<GrantedAuthority> getAuthorities(String token) {
        String [] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier jwtVerifier = getJwtVerifier();
        return jwtVerifier.verify(token).getClaim("authorities").asArray(String.class);
    }

    private JWTVerifier getJwtVerifier() {
        JWTVerifier jwtVerifier;
      try {
          Algorithm algorithm = Algorithm.HMAC512(secret);
          jwtVerifier = JWT.require(algorithm).withIssuer("TEST_COMPANY_NAME").build();
      } catch (JWTVerificationException jwtVerificationException) {
          throw new JWTVerificationException("TOKEN CAN NOT BE VERIFIED!");
      }
        return jwtVerifier;
    }

    private String[] getClaimsFromUser(UserPrincipal user) {
        List<String> authorities = new ArrayList<>();
        for(GrantedAuthority grantedAuthority : user.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }
}
