package com.microservices.demo.springsecurity;

import com.nimbusds.jwt.JWT;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Qualifier
@Service
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
   String auidance = "testUserAuidance";
    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        // Implement your audience validation logic here
        // For example, you can check if the "aud" claim contains the expected audience
        if (token.getAudience().contains(auidance)) {
            return OAuth2TokenValidatorResult.success();
        } else {
            return OAuth2TokenValidatorResult.failure((Collection<OAuth2Error>) new IllegalArgumentException("Invalid audience"));
        }
    }
}
