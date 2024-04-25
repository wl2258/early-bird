package com.ssonzm.userservice.common.util;

import com.ssonzm.userservice.domain.user.User;
import com.ssonzm.userservice.domain.user.UserRole;

public class DummyUtil {
    public User newUser(String username, String email) {
        return User.builder()
                .username(username)
                .email(email)
                .address("서울특별시 종로구")
                .password("$2a$10$lfrIZHgfmHxPXkxqAoa9femfsvO5zoL2zXz.AgtKi3WrHe34pX3DW")
                .phoneNumber("010-1234-5678")
                .role(UserRole.USER)
                .build();
    }
}
