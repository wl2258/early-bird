package com.ssonzm.apigatewayservice.filter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailsDto {
    private Long userId;
    private String role;
}
