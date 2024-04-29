package com.ssonzm.productservice.controller.product;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsFeignClientRespDto;
import com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductListSavedUser;
import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.productservice.service.product.ProductService;
import com.ssonzm.productservice.vo.product.ProductResponseVo.ProductListRespVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductSaveReqDto;
import static com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsRespDto;

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
                                         @RequestHeader("x_user_id") Long userId) {

        Long productId = productService.saveProduct(userId, productSaveReqDto);

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

    @PostMapping("/products/detail")
    public ResponseEntity<ResponseDto<List<ProductDetailsFeignClientRespDto>>> getProductDetailsByIds(@RequestBody List<Long> productIds) {
        List<ProductDetailsFeignClientRespDto> productDetailsByIds = productService.getProductDetailsByIds(productIds);

        ResponseDto<List<ProductDetailsFeignClientRespDto>> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(productDetailsByIds);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProductDetails(@PathVariable Long productId) {
        ProductDetailsRespDto productDetails = productService.getProductDetails(productId);

        ResponseDto<ProductDetailsRespDto> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(productDetails);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/authz/products/my")
    public ResponseEntity<?> getProductListSavedByUser(@RequestHeader("x_user_id") Long userId) {

        ProductListSavedUser productList = productService.getProductSavedByUser(userId);

        ResponseDto<ProductListSavedUser> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(productList);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
