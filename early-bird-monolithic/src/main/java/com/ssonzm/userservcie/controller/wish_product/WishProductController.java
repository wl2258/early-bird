package com.ssonzm.userservcie.controller.wish_product;

import com.ssonzm.userservcie.common.util.ResponseUtil;
import com.ssonzm.userservcie.config.security.PrincipalDetails;
import com.ssonzm.userservcie.dto.common.ResponseDto;
import com.ssonzm.userservcie.service.wish_product.WishProductService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ssonzm.userservcie.dto.wish_product.WishProductRequestDto.*;
import static com.ssonzm.userservcie.vo.wish_product.WishProductResponseVo.*;

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
                                             @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long wishProductId = wishProductService.saveWishProduct(principalDetails.getUser().getId(), wishProductSaveReqDto);

        ResponseDto<Long> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(wishProductId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/authz/wish-products")
    public ResponseEntity<?> getWishProductList(@PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                                                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Page<WishProductListRespVo> wishProductList =
                wishProductService.findWishProductList(principalDetails.getUser().getId(), pageable);

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
