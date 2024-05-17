package com.ssonzm.productservice.controller.wish_product;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.productservice.service.wish_product.WishProductService;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ssonzm.coremodule.vo.wish_product.WishProductResponseVo.WishProductListRespVo;

@RestController
@RequestMapping("/internal")
public class WishProductInternalController {
    private final MessageSource messageSource;
    private final WishProductService wishProductService;

    public WishProductInternalController(MessageSource messageSource, WishProductService wishProductService) {
        this.messageSource = messageSource;
        this.wishProductService = wishProductService;
    }

    @GetMapping("/wish-products/{userId}")
    public ResponseEntity<ResponseDto<Page<WishProductListRespVo>>> getWishProductList(
            @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable("userId") Long userId) {
        Page<WishProductListRespVo> wishProductList =
                wishProductService.findWishProductList(userId, pageable);

        ResponseDto<Page<WishProductListRespVo>> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(wishProductList);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
