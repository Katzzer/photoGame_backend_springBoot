package com.pavelkostal.api.security;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.List.of;

@Component
@AllArgsConstructor
public class AwsCognitoIdTokenProcessor {

    private JwtConfiguration jwtConfiguration;
    private ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;

    public Authentication authenticate(HttpServletRequest request) throws Exception {
        String idToken = request.getHeader(this.jwtConfiguration.getHttpHeader());
        if (idToken != null) {
            JWTClaimsSet claims = this.configurableJWTProcessor.process(this.getBearerToken(idToken),null);
            validateIssuer(claims);
            verifyIfIdToken(claims);
            String username = getUserNameFrom(claims);
            if (username != null) {
                List<GrantedAuthority> grantedAuthorities = of( new SimpleGrantedAuthority("ROLE_ADMIN"));
                User user = new User(username, "", of());
                return new JwtAuthentication(user, claims, grantedAuthorities);
            }
        }
        return null;
    }

    private String getUserNameFrom(JWTClaimsSet claims) {
        return claims.getClaims().get(this.jwtConfiguration.getUserNameField()).toString();
    }

    private void verifyIfIdToken(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception("JWT Token is not an ID Token");
        }
    }

    private void validateIssuer(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(), this.jwtConfiguration.getCognitoIdentityPoolUrl()));
        }
    }

    private String getBearerToken(String token) {
        return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
    }
}
