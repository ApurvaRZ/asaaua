package com.cdac.mumbai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.cdac.mumbai.jwt.JwtAuthenticationEntryPoint;
import com.cdac.mumbai.jwt.JwtAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    public static final String[] WHITE_LIST_URL = {
            "/txns/authCount",
            "/txns/otpCount",
            "/txns/kycCount",
            "/txns/totalCount",
            "/txns/monthWiseCount",
            "/user/register",
            "/user/resetPassword",
            "/user/changePassword",
            "/user/savePassword",
            "/user/deptCount",
            "/user/login",
            "/user/states",
            "/user/refresh",
            "/user/registrationConfirm",
            "/captcha/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/webjars/**",
            "/portal-docs",
            "/swagger-ui.html"
    };

    @Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private LogoutHandler logoutHandler;

    @SuppressWarnings("deprecation")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring SecurityFilterChain");

        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> {
                log.info("Setting authorization rules for requests");
                auth.requestMatchers(WHITE_LIST_URL).permitAll();
                //auth.requestMatchers(WHITE_LIST_URL).permitAll();
              auth.anyRequest().authenticated();
            })
            .exceptionHandling(ex -> {
                log.debug("Configuring exception handling");
                ex.authenticationEntryPoint(point);
            })
            .sessionManagement(session -> {
                log.info("Setting session management to STATELESS");
                session.sessionCreationPolicy(STATELESS);
            })
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> {
                log.info("Configuring logout handling");
                logout.logoutUrl("/user/logout")
                      .addLogoutHandler(logoutHandler)
                      .logoutSuccessHandler((request, response, authentication) -> {
                          log.info("Logout successful, clearing security context");
                          SecurityContextHolder.clearContext();
                      });
            });

        log.info("Security configuration completed successfully");
        return http.build();
    }
}
