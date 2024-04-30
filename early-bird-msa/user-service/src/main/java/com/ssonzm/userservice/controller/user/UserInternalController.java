package com.ssonzm.userservice.controller.user;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.userservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ssonzm.coremodule.dto.user.UserResponseDto.UserDetailsDto;

@Slf4j
@RestController
@RequestMapping("/internal")
public class UserInternalController {
    private final UserService userService;
    private final MessageSource messageSource;

    public UserInternalController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseDto<UserDetailsDto>> getUserDetails(@PathVariable("userId") Long userId) {

        UserDetailsDto userDetails = userService.getUserDetails(userId);

        ResponseDto<UserDetailsDto> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
