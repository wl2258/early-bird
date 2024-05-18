package com.ssonzm.userservcie.common.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(MailMessage mailMessage) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mailMessage.getToAddress());
            message.setSubject(mailMessage.getTitle());
            message.setText(mailMessage.getMessage());

            mailSender.send(message);

            log.info("Sent Email Title : {}", message.getSubject());
        } catch (MailException e) {
            log.error("Fail Sent Email : TO={}, Title={}, Message={}", mailMessage.getToAddress(), mailMessage.getTitle(), mailMessage.getMessage());
            throw new RuntimeException(e);
        }
    }
}
