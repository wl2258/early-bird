package com.ssonzm.productservice.service.user;

import com.ssonzm.productservice.domain.user.User;
import com.ssonzm.productservice.dto.user.UserResponseDto.UserDetailsDto;
import com.ssonzm.productservice.dto.user.UserResponseDto.UserMyPageRespDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import static com.ssonzm.productservice.dto.user.UserRequestDto.*;


public interface UserService extends UserDetailsService {
    User findByIdOrElseThrow(Long userId);
    void signUp(UserSignUpReqDto userSignUpReqDto);

    void updatePassword(Long userId, UserUpdatePwReqDto userUpdatePwReqDto);

    void updateUserInfo(Long userId, UserUpdateReqDto userUpdateReqDto);

    UserDetailsDto getUserDetails(Long userId);

    UserMyPageRespDto getMyPageInfo(Long userId);
}
