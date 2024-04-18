package com.ssonzm.userservcie.common.util;

import com.ssonzm.userservcie.domain.user.UserRole;
import com.ssonzm.userservcie.vo.user.UserLoginVo;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Component
public class SecurityConfigUtil {
    public Set<String> getLoginUserAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    public UserLoginVo getLoginUser() {
        return (UserLoginVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public List<SimpleGrantedAuthority> getUserRoleList(UserRole userRole) {
        return Arrays.asList(userRole.toString()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
