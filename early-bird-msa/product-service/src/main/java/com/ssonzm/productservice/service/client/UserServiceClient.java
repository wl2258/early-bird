package com.ssonzm.productservice.service.client;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.dto.user.UserResponseDto.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/internal/users/{userId}")
    ResponseEntity<ResponseDto<UserDetailsDto>> getUserDetails(@PathVariable("userId") Long userId);
}
