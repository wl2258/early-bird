package com.ssonzm.userservcie.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import static com.ssonzm.userservcie.dto.user.UserRequestDto.*;


public interface UserService extends UserDetailsService {
    void signUp(UserSignUpReqDto userSignUpReqDto);
}
