package com.ssonzm.userservice.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserRequestDto {
    @Data
    public static class UserLoginReqDto {
        @NotNull(message = "이메일을 입력해 주세요")
        @Size(min = 2, message = "2글자 이상 입력해 주세요")
        private String email;

        @NotNull(message = "비밀번호를 입력해 주세요")
        @Size(min = 8, message = "8글자 이상 입력해 주세요")
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class UserSignUpReqDto {
        @NotBlank(message = "이름을 입력해 주세요")
        private String name;

        @Email(message = "잘못된 이메일 형식입니다")
        @NotBlank(message = "이메일을 입력해 주세요")
        @Size(min = 2, message = "2글자 이상 입력해 주세요")
        private String email;

        @NotBlank(message = "비밀번호를 입력해 주세요")
        @Size(min = 8, message = "8글자 이상 입력해 주세요")
        private String password;

        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다 ex) 010-1234-5678")
        private String phoneNumber;

        @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s-_.]{1,100}$", message = "주소 형식이 올바르지 않습니다")
        private String address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdateReqDto {
        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다 ex) 010-1234-5678")
        private String phoneNumber;
        @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s-_.]{1,100}$", message = "주소 형식이 올바르지 않습니다")
        private String address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdatePwReqDto {
        @NotNull(message = "비밀번호를 입력해 주세요")
        @Size(min = 8, message = "8글자 이상 입력해 주세요")
        private String password;
    }
}
