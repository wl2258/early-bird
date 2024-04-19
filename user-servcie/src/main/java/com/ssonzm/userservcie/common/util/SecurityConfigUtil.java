package com.ssonzm.userservcie.common.util;

import com.ssonzm.userservcie.dto.user.UserRequestDto.UserLoginReqDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Component
public class SecurityConfigUtil {
    public Set<String> getLoginUserAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    public UserLoginReqDto getLoginUser() {
        return (UserLoginReqDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
