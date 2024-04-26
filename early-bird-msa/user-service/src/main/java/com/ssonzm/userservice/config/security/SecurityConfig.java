package com.ssonzm.userservice.config.security;

import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.userservice.config.security.filter.AuthenticationFilter;
import com.ssonzm.userservice.config.security.filter.AuthorizationFilter;
import com.ssonzm.userservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final Environment env;
    private final UserService userService;
    private final MessageSource messageSource;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(Environment env, UserService userService,
                          MessageSource messageSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.env = env;
        this.userService = userService;
        this.messageSource = messageSource;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/**").access(
                                        new WebExpressionAuthorizationManager(
                                                "hasIpAddress('127.0.0.1') or hasIpAddress('::1') " +
                                                        "or hasIpAddress('172.30.1.37')"
                                        )
                                )
                                .anyRequest().authenticated()
                )
                .addFilter(getAuthenticationFilter(authenticationManager))
                .authenticationManager(authenticationManager)
                .exceptionHandling((exception) -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                ResponseUtil.fail(response, messageSource, HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((request, response, authException) ->
                                ResponseUtil.fail(response, messageSource, HttpStatus.FORBIDDEN)))
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager, env);
    }

    private AuthorizationFilter getAuthorizationFilter(AuthenticationManager authenticationManager) {
        return new AuthorizationFilter(authenticationManager, env, messageSource);
    }
}
