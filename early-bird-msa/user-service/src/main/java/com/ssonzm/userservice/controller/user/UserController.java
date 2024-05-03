package com.ssonzm.userservice.controller.user;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.dto.user.UserResponseDto.UserMyPageRespDto;
import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.userservice.service.user.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ssonzm.coremodule.dto.user.UserRequestDto.*;
import static com.ssonzm.coremodule.dto.user.UserResponseDto.UserDetailsDto;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final MessageSource messageSource;

    public UserController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @PostMapping("/users")
    public ResponseEntity<?> signUpUser(@RequestBody @Valid UserSignUpReqDto userSignUpReqDto,
                                        BindingResult bindingResult) {
        userService.signUp(userSignUpReqDto);

        ResponseDto<?> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/authz/users/pw")
    public ResponseEntity<?> updatePw(@RequestBody @Valid UserUpdatePwReqDto userUpdatePwReqDto,
                                      BindingResult bindingResult,
                                      @RequestHeader("x_user_id") Long userId) {
        userService.updatePassword(userId, userUpdatePwReqDto);

        ResponseDto<?> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/authz/users")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateReqDto userUpdateReqDto,
                                        BindingResult bindingResult,
                                        @RequestHeader("x_user_id") Long userId) {
        userService.updateUserInfo(userId, userUpdateReqDto);

        ResponseDto<?> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/authz/users")
    public ResponseEntity<?> getUserDetails(@RequestHeader("x_user_id") Long userId) {

        UserDetailsDto userDetails = userService.getUserDetails(userId);

        ResponseDto<UserDetailsDto> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/authz/logout")
    public ResponseEntity<?> logout() {
        ResponseDto<UserDetailsDto> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/authz/users/my-page")
    public ResponseEntity<?> getMyPage(@RequestHeader("x_user_id") Long userId) {
        UserMyPageRespDto userMyPageRespDto = userService.getMyPage(userId);

        ResponseDto<UserMyPageRespDto> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(userMyPageRespDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
