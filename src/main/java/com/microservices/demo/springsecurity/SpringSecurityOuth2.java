package com.microservices.demo.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Allows you to use @PreAuthorize for roles
public class SpringSecurityOuth2 {

@Autowired
private TestUserService testUserService;
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // REST APIs should be stateless
                    )
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/").permitAll()
                            .anyRequest().authenticated()
                    )
                    // Configure the app as an OAuth2 Resource Server
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(jwt->
                                    jwt.jwtAuthenticationConverter
                                            (new TestUserJWTConvertor(testUserService))) // Convert Keycloak roles to Spring Security authorities
                    )
                    .csrf(csrf -> csrf.disable());

            return http.build();
        }
    }
