package com.ssonzm.userservcie.dto.user;

import lombok.Data;

public class UserResponseDto {
    @Data
    public static class UserLoginRespDto {
        private Long id;
        private String email;
        private String name;
    }
}
