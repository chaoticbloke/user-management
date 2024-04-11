package com.users.management.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.users.management.domain.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private String[] getClaimsFromUser(UserPrincipal user) {
        List<String> authorities = new ArrayList<>();
        for(GrantedAuthority grantedAuthority : user.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }
}
