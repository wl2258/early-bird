package com.ssonzm.apigatewayservice.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonzm.apigatewayservice.filter.dto.ResponseDto;
import com.ssonzm.apigatewayservice.filter.dto.UserDetailsDto;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {
    private final Environment env;
    private final String USER_ID_HEADER = "x_user_id";
    private final String USER_ROLE = "USER";
    private final String ADMIN_ROLE = "ADMIN";
    private final String NO_AUTH_MSG = "접근 권한이 없습니다";
    private final String NOT_VALID = "유효성 검사에 실패했습니다";


    public AuthorizationFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String requestUri = request.getURI().toString();
            if (requestUri.contains("/internal/")) {
                return onError(exchange, NO_AUTH_MSG, HttpStatus.UNAUTHORIZED);
            }

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, NO_AUTH_MSG, HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            if (!isJwtValid(jwt)) {
                return onError(exchange, NOT_VALID, HttpStatus.UNAUTHORIZED);
            }

            UserDetailsDto userDetails = verify(jwt);

            String role = userDetails.getRole();
            if (!isAuthorizedAccess(role, request.getPath().value())) {
                return onError(exchange, NO_AUTH_MSG, HttpStatus.FORBIDDEN);
            }

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header(USER_ID_HEADER, String.valueOf(userDetails.getUserId()))
                    .build();

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();

            return chain.filter(modifiedExchange);
        };
    }

    private boolean isAuthorizedAccess(String role, String requestPath) {
        boolean isAuthorized = true;

        if (role.equals(USER_ROLE)) {
            if (requestPath.startsWith("/api/admin"))
                isAuthorized = false;
        } else {
            if (!role.equals(ADMIN_ROLE)) {
                isAuthorized = false;
            }
        }

        return isAuthorized;
    }

    private UserDetailsDto verify (String jwtToken) {
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

             id = claims.get("id", Long.class);
             role = claims.get("role", String.class);

        } catch (Exception ex) {
            log.error("JWT parsing error: {}", ex.getMessage());
        }

        return new UserDetailsDto(id, role);
    }

    private boolean isJwtValid(String jwt) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret").getBytes());
        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        boolean returnValue = true;
        String subject = null;

        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build();

            subject = jwtParser.parseClaimsJws(jwt).getBody().getSubject();
        } catch (Exception ex) {
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ResponseDto responseDto = new ResponseDto(err);
        DataBuffer buffer = null;
        try {
            buffer = response.bufferFactory().wrap(new ObjectMapper().writeValueAsBytes(responseDto));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return response.writeWith(Mono.just(buffer))
                .doOnError(error -> Mono.empty());
    }

    public static class Config {

    }
}
