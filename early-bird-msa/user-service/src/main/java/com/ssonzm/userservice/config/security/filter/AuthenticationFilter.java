package com.ssonzm.userservice.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.userservice.config.security.PrincipalDetails;
import com.ssonzm.userservice.domain.user.User;
import com.ssonzm.coremodule.dto.user.UserRequestDto.UserLoginReqDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;


@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Environment env;
    private final MessageSource messageSource;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                Environment env, MessageSource messageSource) {
        super(authenticationManager);
        this.env = env;
        this.messageSource = messageSource;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication authenticate = null;
        try {
            UserLoginReqDto userLoginReqDto = new ObjectMapper().readValue(request.getInputStream(), UserLoginReqDto.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userLoginReqDto.getEmail(), userLoginReqDto.getPassword()
            );

            authenticate = getAuthenticationManager().authenticate(authenticationToken);

        } catch (Exception e) {
            ResponseUtil.failAuth(response, messageSource, "failAuth");
        }

        return authenticate;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        byte[] secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret").getBytes());

        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Instant now = Instant.now();

        User user = principalDetails.getUser();
        String token = Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("id", user.getId())
                .claim("role", user.getRole().toString())
                .setExpiration(Date.from(now.plusMillis(Long.parseLong(env.getProperty("token.expiration_time")))))
                .setIssuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", String.valueOf(user.getId()));
    }
}
