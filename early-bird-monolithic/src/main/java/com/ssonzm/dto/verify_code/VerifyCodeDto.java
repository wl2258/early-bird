package com.ssonzm.dto.verify_code;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class VerifyCodeDto {

    @Data
    public static class SendEmailCodeReqDto {
        @NotBlank
        @Email(message = "잘못된 이메일 형식입니다")
        private String email;
    }
}
