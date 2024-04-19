package com.ssonzm.userservcie.service.user;

import com.ssonzm.userservcie.dto.user.UserResponseDto.UserDetailsDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import static com.ssonzm.userservcie.dto.user.UserRequestDto.*;


public interface UserService extends UserDetailsService {
    void signUp(UserSignUpReqDto userSignUpReqDto);

    void updatePassword(Long userId, UserUpdatePwReqDto userUpdatePwReqDto);

    void updateUserInfo(Long userId, UserUpdateReqDto userUpdateReqDto);

    UserDetailsDto getUserDetails(Long userId);
}
