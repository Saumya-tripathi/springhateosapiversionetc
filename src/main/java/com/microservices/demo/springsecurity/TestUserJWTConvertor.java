package com.microservices.demo.springsecurity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

public class TestUserJWTConvertor implements Converter<Jwt, AbstractAuthenticationToken>
{
    private static final String REALM_ACCESS_ = "realm_access";
    private static final String ROLES = "roles";
    private static final String Sc = "scope";
    private static final String AUTHORITIES_CLAIM_NAME = "authorities";
//    private static final String SCOPE_AUTHORITY_PREFIX = "SCOPE_";
//    private static final String ROLE_AUTHORITY_PREFIX = "ROLE_";
    private static final String USERNAME_CLAIM_NAME = "preferred_username";
    private static final String SCOPE_SEP="";

    private final TestUserService testUserService;

    public TestUserJWTConvertor(TestUserService service){
        this.testUserService= service;
    }


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        TestUser user = (TestUser) testUserService.loadUserByUsername(jwt.getClaimAsString(USERNAME_CLAIM_NAME));
        List<SimpleGrantedAuthority> roles = getRoles(jwt).stream().map(SimpleGrantedAuthority::new).toList();
        List<SimpleGrantedAuthority> scope = getScope(jwt).stream().map(SimpleGrantedAuthority::new).toList();;
        user.setAuthorities(roles);
        user.setAuthorities(scope);
        return ofNullable(user).map(u->
                new UsernamePasswordAuthenticationToken(u,"NA",u.getAuthorities()))
                .orElseThrow(()->new RuntimeException("User not found"));
    }

    public List<String> getRoles(Jwt jwt) {
        return jwt.getClaims().get(REALM_ACCESS_) != null ?
                (List<String>) ((java.util.Map<String, Object>)
                        jwt.getClaims().get(REALM_ACCESS_)).get(ROLES) : List.of();
    }

    public List<String> getScope(Jwt jwt){
        return jwt.getClaims().get(Sc) != null ?
                (List<String>) ((java.util.Map<String, Object>)
                        jwt.getClaims().get(Sc)) : List.of();
    }
}
