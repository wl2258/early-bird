package com.ssonzm.userservice.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    ADMIN("관리자"), USER("일반사용자");
    private String value;

}
