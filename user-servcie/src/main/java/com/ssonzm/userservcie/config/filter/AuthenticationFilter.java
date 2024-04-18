package com.ssonzm.userservcie.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonzm.userservcie.common.util.SecurityConfigUtil;
import com.ssonzm.userservcie.domain.user.UserRole;
import com.ssonzm.userservcie.dto.user.UserLoginResDto;
import com.ssonzm.userservcie.service.user.UserService;
import com.ssonzm.userservcie.vo.user.UserLoginVo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private Environment env;
    private UserService userService;
    private SecurityConfigUtil securityConfigUtil;

    public AuthenticationFilter(AuthenticationManager authenticationManager, Environment env, UserService userService,
                                SecurityConfigUtil securityConfigUtil) {
        super(authenticationManager);
        this.env = env;
        this.userService = userService;
        this.securityConfigUtil = securityConfigUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginVo userLoginVo = new ObjectMapper().readValue(request.getInputStream(), UserLoginVo.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginVo.getEmail(),
                            userLoginVo.getPassword(),
                            getUserRoles(userLoginVo))
                    );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<SimpleGrantedAuthority> getUserRoles(UserLoginVo userLoginVo) {
        UserRole userRole = userService.getUserRole(userLoginVo.getEmail());
        return securityConfigUtil.getUserRoleList(userRole);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        UserLoginResDto userLoginResDto = userService.getLoginUserDetailsByEmail(username);

        byte[] secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret").getBytes());

        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Instant now = Instant.now();

        String token = Jwts.builder()
                .setSubject(String.valueOf(userLoginResDto.getId()))
                .setExpiration(Date.from(now.plusMillis(Long.parseLong(env.getProperty("token.expiration_time")))))
                .setIssuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", String.valueOf(userLoginResDto.getId()));
    }
}
