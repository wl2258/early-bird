package com.ssonzm.userservcie.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

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
    public static class UserSignUpReqDto {
        @NotBlank(message = "이름을 입력해 주세요")
        private String name;

        @Email(message = "잘못된 이메일 형식입니다")
        @NotBlank(message = "이메일을 입력해 주세요")
        @Size(min = 2, message = "2글자 이상 입력해 주세요")
        private String email;

        @NotBlank(message = "비밀번호를 입력해 주세요")
        private String password;

        @NotBlank(message = "전화번호를 입력해 주세요")
        private String phoneNumber;

        @NotBlank(message = "주소를 입력해 주세요")
        private String address;
    }

    @Data
    public static class UserUpdateReqDto {
        @JsonInclude
        private String phoneNumber;
        @JsonInclude
        private String address;
    }

    @Data
    public static class UserUpdatePwReqDto {
        @NotNull(message = "비밀번호를 입력해 주세요")
        @Size(min = 8, message = "8글자 이상 입력해 주세요")
        private String password;
    }
}
