package com.ssonzm.userservcie.dto.user;


import lombok.Data;

public class UserResponseDto {
    @Data
    public static class UserDetailsDto {
        private String name;
        private String email;
        private String phoneNumber;
        private String address;
    }
}
