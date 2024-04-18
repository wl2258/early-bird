package com.ssonzm.userservcie.dto.user;

import lombok.Data;

public class UserRequestDto {
    @Data
    public static class UserSignUpReqDto {
        private String name;
        private String email;
        private String password;
        private String phoneNumber;
        private String address;
    }
}
