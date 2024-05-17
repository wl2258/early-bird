package com.ssonzm.userservcie.service.verify_code;

public interface VerifyCodeService {
    String sendVerifyCode(String email);
    boolean checkVerifyCode(String email, String verifyCode);
}
