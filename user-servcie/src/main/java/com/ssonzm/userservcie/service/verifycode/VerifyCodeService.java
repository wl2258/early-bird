package com.ssonzm.userservcie.service.verifycode;

public interface VerifyCodeService {
    String sendVerifyCode(String email);
    boolean checkVerifyCode(String email, String verifyCode);
}
