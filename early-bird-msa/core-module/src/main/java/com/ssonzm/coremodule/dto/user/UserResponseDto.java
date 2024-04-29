package com.ssonzm.coremodule.dto.user;

import com.ssonzm.coremodule.vo.product.ProductResponseVo.ProductListRespVo;
import com.ssonzm.coremodule.vo.wish_product.WishProductResponseVo.WishProductListRespVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

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
        private Page<ProductListRespVo> savedProductList;
        private Page<WishProductListRespVo> wishProductListList;
    }
}
