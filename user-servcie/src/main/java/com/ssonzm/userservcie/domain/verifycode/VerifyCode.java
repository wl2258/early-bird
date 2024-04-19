package com.ssonzm.userservcie.domain.verifycode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "emailCode", timeToLive = 210)
public class VerifyCode {
    @Id
    @Indexed
    private String email;

    private String verifyCode;
}
