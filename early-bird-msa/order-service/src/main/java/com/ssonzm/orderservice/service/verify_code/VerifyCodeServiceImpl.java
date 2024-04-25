package com.ssonzm.orderservice.service.verify_code;

import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.orderservice.common.mail.MailMessage;
import com.ssonzm.orderservice.common.mail.MailService;
import com.ssonzm.orderservice.common.util.AesUtil;
import com.ssonzm.orderservice.domain.verify_code.VerifyCode;
import com.ssonzm.orderservice.domain.verify_code.VerifyCodeRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {
    private final int VERIFY_CODE_SIZE = 7;
    private final AesUtil aesUtil;
    private final MailService mailService;
    private final VerifyCodeRepository verifyCodeRepository;

    public VerifyCodeServiceImpl(AesUtil aesUtil, MailService mailService, VerifyCodeRepository verifyCodeRepository) {
        this.aesUtil = aesUtil;
        this.mailService = mailService;
        this.verifyCodeRepository = verifyCodeRepository;
    }

    @Override
    public String sendVerifyCode(String email) {
        String randomCode = generatedRandomCode();
        try {
            MailMessage mailMessage = MailMessage.builder()
                    .toAddress(email)
                    .title("회원가입 인증 번호")
                    .message("회원가입 인증 번호를 입력하세요.\n" + "인증 번호: " + randomCode)
                    .build();
            mailService.sendMail(mailMessage);
        } catch (Exception e) {
            throw new CommonBadRequestException("failSendEmail");
        }

        String encodedEmail = aesUtil.encodeUnique(email);
        verifyCodeRepository.save(new VerifyCode(encodedEmail, randomCode));
        return randomCode;
    }

    private String generatedRandomCode() {
        return UUID.randomUUID().toString().substring(0, VERIFY_CODE_SIZE);
    }

    @Override
    public boolean checkVerifyCode(String email, String verifyCode) {
        String encodedEmail = aesUtil.encodeUnique(email);
        VerifyCode findVerifyCode = verifyCodeRepository.findByEmail(encodedEmail)
                .orElseThrow(() -> new CommonBadRequestException("notValidVerifyCode"));

        return verifyCode.equals(findVerifyCode.getVerifyCode());
    }
}
