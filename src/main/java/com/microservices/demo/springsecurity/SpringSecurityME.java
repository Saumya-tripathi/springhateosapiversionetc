package com.microservices.demo.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//Spring Security Flow:
//Request → Filters Chain → Authentication → Authorization → Resource
//
//Key Beans to Remember:
//- SecurityFilterChain: Defines what to secure and how
//- UserDetailsService: Loads user data for authentication
//- PasswordEncoder: Encodes/decodes passwords
//- AuthenticationManager: Handles authentication logic
//
//Common Configurations:
//- .permitAll() - No security needed
//- .authenticated() - Any logged-in user
//- .hasRole("ADMIN") - Specific role required
//- .httpBasic() - Basic auth popup
//- .formLogin() - Login form
//- .csrf().disable() - Disable CSRF protection======================================================================================================================================================================================
//// Builder pattern in action
//http.authorizeHttpRequests(auth -> auth
//        .requestMatchers("/**").hasRole("USER")  // Chain methods
//    )
//    .httpBasic(Customizer.withDefaults())        // Continue chaining
//    .csrf(csrf -> csrf.disable());               // More chaining
@Configuration
@EnableWebSecurity
public class SpringSecurityME {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(auth -> auth
                            .requestMatchers("/**").hasRole("USER")
                    )
                    .httpBasic(Customizer.withDefaults())
                    .csrf(csrf -> csrf.disable());

            return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            UserDetails user = User.builder()
                    .username("test")
                    //.password("{noop}test") // {noop} is for plain text (use a password encoder in production!) no operation id for noop dnt pass it from bruno
                    .roles("USER")
                    .password("test")
                    .build();
            return new InMemoryUserDetailsManager(user);
        }

        @Bean
    public PasswordEncoder passwordEncoder(){
            return NoOpPasswordEncoder.getInstance();
        }
    }

