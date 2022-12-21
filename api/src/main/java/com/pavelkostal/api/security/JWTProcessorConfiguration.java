package com.pavelkostal.api.security;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

@Configuration
@AllArgsConstructor
public class JWTProcessorConfiguration {
	
	private JwtConfiguration jwtConfiguration;
	
	@Bean
	public ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor() throws MalformedURLException {
		ResourceRetriever resourceRetriever = new DefaultResourceRetriever(jwtConfiguration.getConnectionTimeout(), jwtConfiguration.getReadTimeout());
		URL jwkSetURL= new URL(jwtConfiguration.getJwkUrl());
		JWKSource<SecurityContext> keySource= new RemoteJWKSet<>(jwkSetURL, resourceRetriever);
		ConfigurableJWTProcessor<SecurityContext> jwtProcessor= new DefaultJWTProcessor<>();
		JWSKeySelector<SecurityContext> keySelector= new JWSVerificationKeySelector<>(RS256, keySource);
		jwtProcessor.setJWSKeySelector(keySelector);
		return jwtProcessor;
	}
}
