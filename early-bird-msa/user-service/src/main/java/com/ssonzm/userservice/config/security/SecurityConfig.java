package com.ssonzm.userservice.config.security;

import com.ssonzm.userservice.config.security.filter.AuthenticationFilter;
import com.ssonzm.userservice.config.security.filter.AuthorizationFilter;
import com.ssonzm.userservice.domain.user.UserRole;
import com.ssonzm.userservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                                .requestMatchers(new AntPathRequestMatcher("/api/authz/**")).authenticated()
                                .requestMatchers(new AntPathRequestMatcher ("/admin/**")).hasRole("" + UserRole.ADMIN)
                                .anyRequest().permitAll()
                )
                .addFilter(getAuthenticationFilter(authenticationManager))
                .addFilter(getAuthorizationFilter(authenticationManager))
                .authenticationManager(authenticationManager)
                .build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager, env);
    }

    private AuthorizationFilter getAuthorizationFilter(AuthenticationManager authenticationManager) {
        return new AuthorizationFilter(authenticationManager, env, messageSource);
    }
}
