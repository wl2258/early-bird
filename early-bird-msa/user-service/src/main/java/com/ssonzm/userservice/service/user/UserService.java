package com.ssonzm.userservice.service.user;

import com.ssonzm.userservice.domain.user.User;
import com.ssonzm.userservice.dto.user.UserResponseDto.UserDetailsDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import static com.ssonzm.userservice.dto.user.UserRequestDto.*;


public interface UserService extends UserDetailsService {
    User findByIdOrElseThrow(Long userId);
    void signUp(UserSignUpReqDto userSignUpReqDto);

    void updatePassword(Long userId, UserUpdatePwReqDto userUpdatePwReqDto);

    void updateUserInfo(Long userId, UserUpdateReqDto userUpdateReqDto);

    UserDetailsDto getUserDetails(Long userId);

//    UserMyPageRespDto getMyPageInfo(Long userId);
}
