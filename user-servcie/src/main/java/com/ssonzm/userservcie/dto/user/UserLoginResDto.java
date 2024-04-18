package com.ssonzm.userservcie.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserLoginResDto {
    private Long id;
    private String email;
    private String name;
}
