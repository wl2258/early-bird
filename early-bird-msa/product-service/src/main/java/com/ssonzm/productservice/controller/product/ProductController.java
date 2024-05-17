package com.ssonzm.productservice.controller.product;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductUpdateReqDto;
import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.coremodule.vo.product.ProductResponseVo.ProductListRespVo;
import com.ssonzm.productservice.service.product.ProductService;
import com.ssonzm.productservice.service.product.RedissonLockProductFacade;
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

import static com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductSaveReqDto;
import static com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsRespDto;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProductController {
    private final MessageSource messageSource;
    private final ProductService productService;
    private final RedissonLockProductFacade productFacade;

    public ProductController(MessageSource messageSource, ProductService productService,
                             RedissonLockProductFacade productFacade) {
        this.messageSource = messageSource;
        this.productService = productService;
        this.productFacade = productFacade;
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

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProductDetails(@PathVariable("productId") Long productId) {
        ProductDetailsRespDto productDetails = productService.getProductDetails(productId);

        ResponseDto<ProductDetailsRespDto> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(productDetails);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/products/quantity/{productId}")
    public ResponseEntity<?> getProductQuantity(@PathVariable("productId") Long productId) {
        Long quantity = productFacade.getProductQuantity(productId);

        ResponseDto<Long> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(quantity);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/authz/products")
    public ResponseEntity<?> updateProducts(@RequestBody @Valid ProductUpdateReqDto productUpdateReqDto,
                                            BindingResult bindingResult) {
        productService.updateProductInfo(productUpdateReqDto);

        ResponseDto<ProductDetailsRespDto> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/authz/products/resv")
    public ResponseEntity<?> orderProduct(@RequestBody @Valid OrderProductUpdateReqDto orderProductRequestDto,
                                          BindingResult bindingResult,
                                          @RequestHeader("x_user_id") Long userId) {
        productFacade.decreaseProductQuantity(userId, orderProductRequestDto);

        ResponseDto<ProductDetailsRespDto> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
