package com.ssonzm.userservcie.service.user;

import com.ssonzm.userservcie.domain.user.UserRole;
import org.springframework.security.core.userdetails.UserDetailsService;

import static com.ssonzm.userservcie.dto.user.UserResponseDto.*;


public interface UserService extends UserDetailsService {
    UserRole getUserRole(String email);

    UserLoginRespDto getLoginUserDetailsByEmail(String email);
}
