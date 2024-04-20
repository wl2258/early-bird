package com.ssonzm.userservcie.service.user;

import com.ssonzm.userservcie.domain.user.User;
import com.ssonzm.userservcie.dto.user.UserResponseDto.UserDetailsDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import static com.ssonzm.userservcie.dto.user.UserRequestDto.*;


public interface UserService extends UserDetailsService {
    User findByIdOrElseThrow(Long userId);
    void signUp(UserSignUpReqDto userSignUpReqDto);

    void updatePassword(Long userId, UserUpdatePwReqDto userUpdatePwReqDto);

    void updateUserInfo(Long userId, UserUpdateReqDto userUpdateReqDto);

    UserDetailsDto getUserDetails(Long userId);
}
