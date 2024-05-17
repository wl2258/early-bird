package com.ssonzm.productservice.service.client;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.dto.user.UserResponseDto.UserDetailsDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    String UNKNOWN_USER_NAME = "Unknown";
    Logger log = LoggerFactory.getLogger(UserServiceClient.class);

    @GetMapping("/internal/users/{userId}")
    @CircuitBreaker(name = "userCircuitBreaker", fallbackMethod = "failGetUserDetails")
    ResponseEntity<ResponseDto<UserDetailsDto>> getUserDetails(@PathVariable("userId") Long userId);

    default ResponseEntity<ResponseDto<UserDetailsDto>> failGetUserDetails(Exception e) {
        log.error("[UserServiceClient] getUserDetails() 호출 실패");
        ResponseDto<UserDetailsDto> responseDto = new ResponseDto<>();

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setName(UNKNOWN_USER_NAME);

        responseDto.setBody(userDetailsDto);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseDto);
    }
}
