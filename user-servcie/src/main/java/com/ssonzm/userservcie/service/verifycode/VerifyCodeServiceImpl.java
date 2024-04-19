package com.ssonzm.userservcie.service.verifycode;

import com.ssonzm.userservcie.common.exception.CommonBadRequestException;
import com.ssonzm.userservcie.common.mail.MailMessage;
import com.ssonzm.userservcie.common.mail.MailService;
import com.ssonzm.userservcie.domain.verifycode.VerifyCode;
import com.ssonzm.userservcie.domain.verifycode.VerifyCodeRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    private final MailService mailService;
    private final VerifyCodeRepository verifyCodeRepository;

    public VerifyCodeServiceImpl(MailService mailService, VerifyCodeRepository verifyCodeRepository) {
        this.mailService = mailService;
        this.verifyCodeRepository = verifyCodeRepository;
    }

    @Override
    public String sendVerifyCode(String email) {
        VerifyCode verifyCode = verifyCodeRepository.findByEmail(email).orElse(null);
        if (verifyCode != null) throw new CommonBadRequestException("failSendEmail");

        String randomCode = UUID.randomUUID().toString().substring(0, 7);

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

        verifyCodeRepository.save(new VerifyCode(email, randomCode));
        return randomCode;
    }

    @Override
    public boolean checkVerifyCode(String email, String verifyCode) {
        VerifyCode findVerifyCode = verifyCodeRepository.findByEmail(email)
                .orElseThrow(() -> new CommonBadRequestException("notValidVerifyCode"));

        return verifyCode.equals(findVerifyCode.getVerifyCode());
    }
}
