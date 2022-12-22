package com.pavelkostal.api.tool;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@AllArgsConstructor
public class TokenTool {
	
	private ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;
	
	private String getTokenFromBearer(String token) {
		return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
	}
	
	public String getUniqueUserId(String token) throws BadJOSEException, ParseException, JOSEException {
		JWTClaimsSet claims = configurableJWTProcessor.process(getTokenFromBearer(token), null);
		return claims.getSubject();
	}
}
