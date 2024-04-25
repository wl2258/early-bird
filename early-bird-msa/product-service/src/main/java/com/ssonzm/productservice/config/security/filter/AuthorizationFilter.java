package com.ssonzm.productservice.config.security.filter;

import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.productservice.config.security.PrincipalDetails;
import com.ssonzm.productservice.domain.user.User;
import com.ssonzm.productservice.domain.user.UserRole;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {
    private String TOKEN_PREFIX = "Bearer ";
    private final Environment env;
    private final MessageSource messageSource;

    public AuthorizationFilter(AuthenticationManager authenticationManager, Environment env, MessageSource messageSource) {
        super(authenticationManager);
        this.env = env;
        this.messageSource = messageSource;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

/*if (!isRequestValid(request)) {
            filterChain.doFilter(request, response);
            return;
        }*/
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (authorizationHeader != null) {
                String jwtToken = authorizationHeader.replace(TOKEN_PREFIX, "");
                setAuthenticationFromAccessToken(jwtToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            ResponseUtil.fail(response, messageSource, HttpStatus.BAD_REQUEST);
        }
    }

    private void setAuthenticationFromAccessToken(String jwtToken) {
        PrincipalDetails loginUser = verify(jwtToken);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginUser, null, loginUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isRequestValid(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        return isApiAuthRequest(requestUrl) || isAdminRequest(requestUrl);
    }

    private boolean isApiAuthRequest(String requestUrl) {
        return requestUrl.contains("/api/authz/");
    }

    private boolean isAdminRequest(String requestUrl) {
        return requestUrl.contains("/admin/");
    }

    private PrincipalDetails verify (String jwtToken) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret").getBytes());
        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        Long id = null;
        String role = null;
        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build();

            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(jwtToken);
            Claims claims = claimsJws.getBody();

            id = claims.get("id", Long.class); // "id"
            role = claims.get("role", String.class); // "role"

        } catch (Exception ex) {
            log.error("JWT parsing error: {}", ex.getMessage());
        }

        User user = User.builder().id(id).role(UserRole.valueOf(role)).build();
        return new PrincipalDetails(user);
    }

}
