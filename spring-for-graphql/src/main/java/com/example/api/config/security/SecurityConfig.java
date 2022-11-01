package com.example.api.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity // Debug = true, will print the execution of the FilterChainProxy
@Slf4j
public class SecurityConfig {

    public static final String USER_ID_PRE_AUTH_HEADER = "user_id";
    public static final String USER_ROLES_PRE_AUTH_HEADER = "user_roles";
    public static final String CORRELATION_ID = "correlation_ID";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .authorizeRequests(ac -> {
                    ac.antMatchers("/graphql", "/personaQL", "/vendor/personaQL/**", "/graphiql/**", "/graphql**",
                                    "/subscriptions/**", "/vendor/**", "/graphiql-subscriptions-fetcher@0.0.2/**",
                                    "/subscriptions-transport-ws@0.8.3/**", "/altair/**")
                            .hasAnyRole("USER", "MANAGER", "ADMIN");
                    // All endpoints require authentication
                    ac.anyRequest().authenticated();
                })
                // Disable CSRF Token generation
                .csrf().disable()
                // Disable the default HTTP Basic-Auth
                .httpBasic()
                .and()
                // Disable the /logout filter
                .logout().disable()
                // Disable anonymous users
                .anonymous().disable();
        http.authenticationManager(authenticationManager);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder.encode("user"))
                .roles("USER")
                .and()
                .withUser("manager")
                .password(passwordEncoder.encode("manager"))
                .roles("MANAGER", "USER")
                .and()
                .withUser("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN", "MANAGER", "USER");
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
