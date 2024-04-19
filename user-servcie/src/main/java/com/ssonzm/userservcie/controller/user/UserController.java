package com.ssonzm.userservcie.controller.user;

import com.ssonzm.userservcie.common.util.ResponseUtil;
import com.ssonzm.userservcie.common.util.SecurityConfigUtil;
import com.ssonzm.userservcie.domain.user.User;
import com.ssonzm.userservcie.dto.common.ResponseDto;
import com.ssonzm.userservcie.service.user.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ssonzm.userservcie.dto.user.UserRequestDto.*;
import static com.ssonzm.userservcie.dto.user.UserResponseDto.*;

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
                                      BindingResult bindingResult) {
        User loginUser = securityConfigUtil.getLoginUser().getUser();
        userService.updatePassword(loginUser.getId(), userUpdatePwReqDto);

        ResponseDto<?> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/authz/users")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateReqDto userUpdateReqDto,
                                      BindingResult bindingResult) {
        User loginUser = securityConfigUtil.getLoginUser().getUser();
        userService.updateUserInfo(loginUser.getId(), userUpdateReqDto);

        ResponseDto<?> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/authz/users")
    public ResponseEntity<?> getUserDetails() {
        User loginUser = securityConfigUtil.getLoginUser().getUser();
        UserDetailsDto userDetails = userService.getUserDetails(loginUser.getId());

        ResponseDto<UserDetailsDto> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
