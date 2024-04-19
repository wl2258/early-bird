package com.ssonzm.userservcie.common.mail;

import lombok.*;

@Data
@NoArgsConstructor
@ToString(of = {"toAddress", "title"})
public class MailMessage {
    private String toAddress;
    private String title;
    private String message;

    @Builder
    public MailMessage(String toAddress, String title, String message) {
        this.toAddress = toAddress;
        this.title = title;
        this.message = message;
    }
}