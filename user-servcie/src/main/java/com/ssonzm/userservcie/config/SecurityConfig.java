package com.ssonzm.userservcie.config;

import com.ssonzm.userservcie.common.util.SecurityConfigUtil;
import com.ssonzm.userservcie.config.filter.AuthenticationFilter;
import com.ssonzm.userservcie.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private Environment env;
    private UserService userService;
    private SecurityConfigUtil securityConfigUtil;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(Environment env, UserService userService, SecurityConfigUtil securityConfigUtil,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.env = env;
        this.userService = userService;
        this.securityConfigUtil = securityConfigUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private static final String[] PATH_LIST = {
            "/**",
    };

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
                                .requestMatchers(PATH_LIST).permitAll()
                )
                .addFilter(getAuthenticationFilter(authenticationManager))
                .authenticationManager(authenticationManager)
                .build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager, env, userService, securityConfigUtil);
    }

}
