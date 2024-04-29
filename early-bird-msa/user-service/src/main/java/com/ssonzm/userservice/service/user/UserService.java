package com.ssonzm.userservice.service.user;

import com.ssonzm.coremodule.dto.user.UserResponseDto.UserDetailsDto;
import com.ssonzm.coremodule.dto.user.UserResponseDto.UserMyPageRespDto;
import com.ssonzm.userservice.domain.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import static com.ssonzm.coremodule.dto.user.UserRequestDto.*;


public interface UserService extends UserDetailsService {
    User findByIdOrElseThrow(Long userId);

    void signUp(UserSignUpReqDto userSignUpReqDto);

    void updatePassword(Long userId, UserUpdatePwReqDto userUpdatePwReqDto);

    void updateUserInfo(Long userId, UserUpdateReqDto userUpdateReqDto);

    UserDetailsDto getUserDetails(Long userId);

    UserMyPageRespDto getMyPage(Long userId);
}
