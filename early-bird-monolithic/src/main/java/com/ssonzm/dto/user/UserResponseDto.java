package com.ssonzm.dto.user;

import com.ssonzm.dto.product.ProductResponseDto.ProductDetailsRespDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public class UserResponseDto {
    @Data
    public static class UserDetailsDto {
        private String name;
        private String email;
        private String phoneNumber;
        private String address;
    }

    @Data
    @AllArgsConstructor
    public static class UserMyPageRespDto {
        private List<ProductDetailsRespDto> myProductList; // 내가 등록한 상품
    }
}
