package com.ssonzm.userservcie.service.user;

import com.ssonzm.userservcie.common.exception.CommonBadRequestException;
import com.ssonzm.userservcie.common.util.SecurityConfigUtil;
import com.ssonzm.userservcie.domain.user.UserRepository;
import com.ssonzm.userservcie.domain.user.UserRole;
import com.ssonzm.userservcie.domain.user.Users;
import com.ssonzm.userservcie.dto.user.UserResponseDto.UserLoginRespDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private SecurityConfigUtil securityConfigUtil;

    public UserServiceImpl(UserRepository userRepository, SecurityConfigUtil securityConfigUtil) {
        this.userRepository = userRepository;
        this.securityConfigUtil = securityConfigUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users findUser = userRepository.findByEmail(username).orElseThrow(() -> new CommonBadRequestException("notFoundUser"));

        return new User(findUser.getEmail(), findUser.getPassword(),
                true, true, true, true,
                securityConfigUtil.getUserRoleList(findUser.getRole()));
    }

    @Override
    public UserRole getUserRole(String email) {
        Users findUser = userRepository.findByEmail(email).orElseThrow(() -> new CommonBadRequestException("notFoundUser"));
        return findUser.getRole();
    }

    @Override
    public UserLoginRespDto getLoginUserDetailsByEmail(String email) {
        Users findUser = userRepository.findByEmail(email).orElseThrow(() -> new CommonBadRequestException("notFoundUser"));
        return new ModelMapper().map(findUser, UserLoginRespDto.class);
    }
}
