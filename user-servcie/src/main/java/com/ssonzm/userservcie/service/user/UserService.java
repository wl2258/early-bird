package com.ssonzm.userservcie.service.user;

import com.ssonzm.userservcie.domain.user.UserRole;
import com.ssonzm.userservcie.dto.user.UserLoginResDto;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    UserRole getUserRole(String email);

    UserLoginResDto getLoginUserDetailsByEmail(String email);
}
