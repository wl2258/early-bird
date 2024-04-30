package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.dto.order_product.OrderProjectRequestDto.OrderProductUpdateReqDto;
import com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsFeignClientRespDto;
import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.productservice.domain.product.Product;
import com.ssonzm.productservice.domain.product.ProductCategory;
import com.ssonzm.productservice.domain.product.ProductRepository;
import com.ssonzm.productservice.domain.product.ProductStatus;
import com.ssonzm.productservice.service.client.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductSaveReqDto;
import static com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsRespDto;
import static com.ssonzm.coremodule.dto.user.UserResponseDto.UserDetailsDto;
import static com.ssonzm.coremodule.vo.product.ProductResponseVo.ProductListRespVo;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserServiceClient userServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public ProductServiceImpl(ProductRepository productRepository, UserServiceClient userServiceClient,
                              CircuitBreakerFactory circuitBreakerFactory) {
        this.productRepository = productRepository;
        this.userServiceClient = userServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    @Transactional
    public Long saveProduct(Long userId, ProductSaveReqDto productSaveReqDto) {
        Product product = createProduct(productSaveReqDto, userId);
        productRepository.save(product);
        return product.getId();
    }

    private Product createProduct(ProductSaveReqDto productSaveReqDto, Long userId) {
        int quantity = productSaveReqDto.getQuantity();
        ProductStatus status = quantity > 0 ? ProductStatus.IN_STOCK : ProductStatus.SOLD_OUT;
        return Product.builder()
                .userId(userId)
                .name(productSaveReqDto.getProductName())
                .category(ProductCategory.valueOf(productSaveReqDto.getCategory()))
                .status(status)
                .description(productSaveReqDto.getDescription())
                .price(productSaveReqDto.getPrice())
                .quantity(quantity)
                .build();
    }

    @Override
    public Page<ProductListRespVo> getProductList(Pageable pageable) {
        return productRepository.getProductList(pageable);
    }

    @Override
    public ProductDetailsRespDto getProductDetails(Long productId) {
        Product findProduct = findProductByIdOrElseThrow(productId);
        return createProductDetailsRespDto(findProduct);
    }

    @Override
    public List<ProductDetailsFeignClientRespDto> getProductDetailsByIds(List<Long> productIds) {
        List<Product> productList = productRepository.findAllByIds(productIds);
        return productList.stream()
                .map(p -> new ModelMapper().map(p, ProductDetailsFeignClientRespDto.class))
                .toList();
    }

    @Override
    public Product findProductByIdOrElseThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CommonBadRequestException("notFoundData"));
    }

    @Override
    public Page<ProductListRespVo> getProductSavedByUser(Pageable pageable, Long userId) {
        return productRepository.getProductListByUser(pageable, userId);
    }

    @Override
    @Transactional
    public void updateProductQuantity(List<OrderProductUpdateReqDto> orderProductUpdateList) {
        for (OrderProductUpdateReqDto updateDto : orderProductUpdateList) {
            Product findProduct = findProductByIdOrElseThrow(updateDto.getProductId());
            findProduct.updateQuantity(updateDto.getQuantity());
        }
    }

    private ProductDetailsRespDto createProductDetailsRespDto(Product product) {
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("user-circuit-breaker");

        UserDetailsDto userDetailsDto = circuitbreaker
                .run(() -> userServiceClient.getUserDetails(product.getUserId()).getBody().getBody(),
                        throwable -> new UserDetailsDto());

        return new ProductDetailsRespDto(product.getId(), userDetailsDto.getName(), product.getName(),
                String.valueOf(product.getCategory()), product.getDescription(), product.getQuantity(),
                product.getPrice(), product.getCreatedDate());
    }
}
