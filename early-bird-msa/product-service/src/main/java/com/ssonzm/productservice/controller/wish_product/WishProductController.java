package com.ssonzm.productservice.controller.wish_product;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.productservice.service.wish_product.WishProductService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ssonzm.coremodule.dto.wish_product.WishProductRequestDto.WishProductSaveReqDto;
import static com.ssonzm.coremodule.dto.wish_product.WishProductRequestDto.WishProductUpdateReqDto;
import static com.ssonzm.coremodule.vo.wish_product.WishProductResponseVo.WishProductListRespVo;

@RestController
@RequestMapping("/api")
public class WishProductController {
    private final MessageSource messageSource;
    private final WishProductService wishProductService;

    public WishProductController(MessageSource messageSource, WishProductService wishProductService) {
        this.messageSource = messageSource;
        this.wishProductService = wishProductService;
    }

    @PostMapping("/authz/wish-products")
    public ResponseEntity<?> saveWishProduct(@RequestBody @Valid WishProductSaveReqDto wishProductSaveReqDto,
                                             BindingResult bindingResult,
                                             @RequestHeader("x_user_id") Long userId) {

        Long wishProductId = wishProductService.saveWishProduct(userId, wishProductSaveReqDto);

        ResponseDto<Long> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(wishProductId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
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

    @PatchMapping("/authz/wish-products")
    public ResponseEntity<?> updateWishProductQuantity(@RequestBody @Valid WishProductUpdateReqDto wishProductUpdateReqDto,
                                                       BindingResult bindingResult) {
        wishProductService.updateQuantity(wishProductUpdateReqDto);
        ResponseDto<?> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/authz/wish-products/{wishProductId}")
    public ResponseEntity<?> deleteWishProduct(@PathVariable Long wishProductId) {
        wishProductService.deleteWishProduct(wishProductId);
        ResponseDto<?> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
