package com.ssonzm.controller.product;

import com.ssonzm.common.util.ResponseUtil;
import com.ssonzm.config.security.PrincipalDetails;
import com.ssonzm.dto.common.ResponseDto;
import com.ssonzm.dto.product.ProductRequestDto;
import com.ssonzm.dto.product.ProductResponseDto;
import com.ssonzm.service.product.ProductService;
import com.ssonzm.vo.product.ProductResponseVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/api")
public class ProductController {

    private final MessageSource messageSource;
    private final ProductService productService;

    public ProductController(MessageSource messageSource, ProductService productService) {
        this.messageSource = messageSource;
        this.productService = productService;
    }

    @PostMapping("/authz/products")
    public ResponseEntity<?> saveProduct(@RequestBody @Valid ProductRequestDto.ProductSaveReqDto productSaveReqDto,
                                         BindingResult bindingResult,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long productId = productService.saveProduct(principalDetails.getUser().getId(), productSaveReqDto);

        ResponseDto<Long> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProductList(
            @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC)Pageable pageable) {
        Page<ProductResponseVo.ProductListRespVo> productList = productService.getProductList(pageable);

        ResponseDto<Page<ProductResponseVo.ProductListRespVo>> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(productList);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProductDetails(@PathVariable Long productId) {
        ProductResponseDto.ProductDetailsRespDto productDetails = productService.getProductDetails(productId);

        ResponseDto<ProductResponseDto.ProductDetailsRespDto> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(productDetails);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
