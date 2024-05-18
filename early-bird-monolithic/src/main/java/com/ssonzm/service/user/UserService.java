package com.ssonzm.service.user;

import com.ssonzm.domain.user.User;
import com.ssonzm.dto.user.UserResponseDto.UserDetailsDto;
import com.ssonzm.dto.user.UserResponseDto.UserMyPageRespDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import static com.ssonzm.dto.user.UserRequestDto.*;


public interface UserService extends UserDetailsService {
    User findByIdOrElseThrow(Long userId);
    void signUp(UserSignUpReqDto userSignUpReqDto);

    void updatePassword(Long userId, UserUpdatePwReqDto userUpdatePwReqDto);

    void updateUserInfo(Long userId, UserUpdateReqDto userUpdateReqDto);

    UserDetailsDto getUserDetails(Long userId);

    UserMyPageRespDto getMyPageInfo(Long userId);
}
