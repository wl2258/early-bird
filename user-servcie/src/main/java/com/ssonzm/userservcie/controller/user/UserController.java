package com.ssonzm.userservcie.controller.user;

import com.ssonzm.userservcie.common.util.ResponseUtil;
import com.ssonzm.userservcie.common.util.SecurityConfigUtil;
import com.ssonzm.userservcie.dto.common.ResponseDto;
import com.ssonzm.userservcie.service.user.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.ssonzm.userservcie.dto.user.UserRequestDto.*;

@Slf4j
@Controller
public class UserController {
    private final UserService userService;
    private final MessageSource messageSource;
    private final SecurityConfigUtil securityConfigUtil;

    public UserController(UserService userService, MessageSource messageSource, SecurityConfigUtil securityConfigUtil) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.securityConfigUtil = securityConfigUtil;
    }

    @PostMapping("/users")
    public ResponseEntity<?> signUpUser(@RequestBody @Valid UserSignUpReqDto userSignUpReqDto,
                                        BindingResult bindingResult) {

        userService.signUp(userSignUpReqDto);

        ResponseDto<?> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
