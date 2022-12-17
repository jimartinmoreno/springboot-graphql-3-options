package com.example.api.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity // Debug = true, will print the execution of the FilterChainProxy
@Slf4j
public class SecurityConfig {

    public static final String USER_ID_PRE_AUTH_HEADER = "user_id";
    public static final String USER_ROLES_PRE_AUTH_HEADER = "user_roles";

    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, Filter createRequestHeadersPreAuthenticationFilter, AuthenticationManager authenticationManager) throws Exception {
    public SecurityFilterChain securityFilterChain(HttpSecurity http, InMemoryUserDetailsManager userDetailsService) throws Exception {
        http
                .userDetailsService(userDetailsService)
                //.authenticationManager(authenticationManager);
                //.addFilterBefore(createRequestHeadersPreAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class)
                .securityContext((securityContext) -> securityContext
                        .requireExplicitSave(true)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/graphql/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .anonymous().disable();
        return http.build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.inMemoryAuthentication()
//                .withUser("user")
//                .password(passwordEncoder.encode("user"))
//                .roles("USER")
//                .and()
//                .withUser("manager")
//                .password(passwordEncoder.encode("manager"))
//                .roles("MANAGER", "USER")
//                .and()
//                .withUser("admin")
//                .password(passwordEncoder.encode("admin"))
//                .roles("ADMIN", "MANAGER", "USER");
//        return authenticationManagerBuilder.build();
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
//        var authProvider = new DaoAuthenticationProvider();
//        authProvider.setPasswordEncoder(passwordEncoder);
//        authProvider.setUserDetailsService(userDetailsService);
//        return new ProviderManager(authProvider);
//    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
//                .passwordEncoder(passwordEncoder()::encode)
                .roles("ADMIN", "MANAGER", "USER")
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("user")
//                .passwordEncoder(passwordEncoder()::encode)
                .roles("USER")
                .build();

        UserDetails manager = User.withDefaultPasswordEncoder()
                .username("manager")
                .password("manager")
//                .passwordEncoder(passwordEncoder()::encode)
                .roles("MANAGER", "USER")
                .build();

        return new InMemoryUserDetailsManager(admin, manager, user);
    }

    //@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean
//    public Filter createRequestHeadersPreAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
//        log.info("@@@@@@@@@@@@@@@@@@@ createRequestHeadersPreAuthenticationFilter() @@@@@@@@@@@@@@@@@@@@@@");
//        var filter = new RequestHeadersPreAuthenticationFilter();
//        filter.setAuthenticationDetailsSource(new GrantedAuthoritiesAuthenticationDetailsSource());
//        filter.setContinueFilterChainOnUnsuccessfulAuthentication(false);
//        filter.setAuthenticationManager(authenticationManager);
//        return filter;
//    }
}
