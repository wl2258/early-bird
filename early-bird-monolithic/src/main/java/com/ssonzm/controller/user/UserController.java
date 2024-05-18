package com.ssonzm.controller.user;

import com.ssonzm.common.util.ResponseUtil;
import com.ssonzm.common.util.SecurityConfigUtil;
import com.ssonzm.config.security.PrincipalDetails;
import com.ssonzm.dto.common.ResponseDto;
import com.ssonzm.service.user.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ssonzm.dto.user.UserRequestDto.*;
import static com.ssonzm.dto.user.UserResponseDto.UserDetailsDto;
import static com.ssonzm.dto.user.UserResponseDto.UserMyPageRespDto;

@Slf4j
@RestController
@RequestMapping("/api")
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

    @PatchMapping("/authz/users/pw")
    public ResponseEntity<?> updatePw(@RequestBody @Valid UserUpdatePwReqDto userUpdatePwReqDto,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.updatePassword(principalDetails.getUser().getId(), userUpdatePwReqDto);

        ResponseDto<?> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/authz/users")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateReqDto userUpdateReqDto,
                                        BindingResult bindingResult,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.updateUserInfo(principalDetails.getUser().getId(), userUpdateReqDto);

        ResponseDto<?> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/authz/users")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserDetailsDto userDetails = userService.getUserDetails(principalDetails.getUser().getId());

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
    public ResponseEntity<?> getMyPage(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserMyPageRespDto myPageRespDto = userService.getMyPageInfo(principalDetails.getUser().getId());

        ResponseDto<UserMyPageRespDto> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(myPageRespDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
