package com.ssonzm.userservcie.controller.controller;

import com.ssonzm.userservcie.common.util.ResponseUtil;
import com.ssonzm.userservcie.config.security.PrincipalDetails;
import com.ssonzm.userservcie.dto.common.ResponseDto;
import com.ssonzm.userservcie.service.product.ProductService;
import com.ssonzm.userservcie.vo.product.ProductResponseVo.ProductListRespVo;
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

import static com.ssonzm.userservcie.dto.product.ProductRequestDto.*;

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
    public ResponseEntity<?> saveProduct(@RequestBody @Valid ProductSaveReqDto productSaveReqDto,
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
        Page<ProductListRespVo> productList = productService.getProductList(pageable);

        ResponseDto<Page<ProductListRespVo>> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(productList);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
