package com.ssonzm.productservice.controller.product;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.productservice.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsFeignClientRespDto;
import static com.ssonzm.coremodule.vo.product.ProductResponseVo.ProductListRespVo;

@Slf4j
@RestController
@RequestMapping("/internal")
public class ProductInternalController {
    private final MessageSource messageSource;
    private final ProductService productService;

    public ProductInternalController(MessageSource messageSource, ProductService productService) {
        this.messageSource = messageSource;
        this.productService = productService;
    }

    @PostMapping("/products/detail")
    public ResponseEntity<ResponseDto<List<ProductDetailsFeignClientRespDto>>> getProductDetailsByIds(@RequestBody List<Long> productIds) {
        List<ProductDetailsFeignClientRespDto> productDetailsByIds = productService.getProductDetailsByIds(productIds);

        ResponseDto<List<ProductDetailsFeignClientRespDto>> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(productDetailsByIds);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/products/my/{userId}")
    public ResponseEntity<ResponseDto<Page<ProductListRespVo>>> getProductListSavedByUser(
            @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC)Pageable pageable,
            @PathVariable("userId") Long userId) {

        Page<ProductListRespVo> productSavedByUser = productService.getProductSavedByUser(pageable, userId);

        ResponseDto<Page<ProductListRespVo>> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(productSavedByUser);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
