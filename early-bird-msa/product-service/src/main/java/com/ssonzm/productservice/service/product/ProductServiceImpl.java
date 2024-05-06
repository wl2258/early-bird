package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductUpdateReqDto;
import com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsFeignClientRespDto;
import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.coremodule.vo.KafkaVo;
import com.ssonzm.productservice.domain.product.Product;
import com.ssonzm.productservice.domain.product.ProductCategory;
import com.ssonzm.productservice.domain.product.ProductRepository;
import com.ssonzm.productservice.domain.product.ProductStatus;
import com.ssonzm.productservice.service.client.UserServiceClient;
import com.ssonzm.productservice.service.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ssonzm.coremodule.dto.order.kafka.OrderRequestDto.OrderSaveKafkaReqDto;
import static com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductSaveReqDto;
import static com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsRespDto;
import static com.ssonzm.coremodule.dto.user.UserResponseDto.UserDetailsDto;
import static com.ssonzm.coremodule.vo.product.ProductResponseVo.ProductListRespVo;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final KafkaSender kafkaSender;
    private final ProductRepository productRepository;
    private final UserServiceClient userServiceClient;
    private final ProductRedisService productRedisService;


    public ProductServiceImpl(KafkaSender kafkaSender, ProductRepository productRepository,
                              UserServiceClient userServiceClient, ProductRedisService productRedisService) {
        this.kafkaSender = kafkaSender;
        this.productRepository = productRepository;
        this.userServiceClient = userServiceClient;
        this.productRedisService = productRedisService;
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
                .reservationStartTime(productSaveReqDto.getReservationStartTime())
                .build();
    }

    @Override
    public Page<ProductListRespVo> getProductList(Pageable pageable) {
        return productRepository.getProductList(pageable);
    }

    @Override
    public ProductDetailsRespDto getProductDetails(Long productId) {
        Product findProduct = findProductByIdOrElseThrow(productId);
        return getProductDetailsRespDto(findProduct);
    }

    private ProductDetailsRespDto getProductDetailsRespDto(Product product) {
        UserDetailsDto userDetailsDto = userServiceClient.getUserDetails(product.getUserId()).getBody().getBody();
        return createProductDetailsRespDto(product, userDetailsDto.getName());
    }

    private ProductDetailsRespDto createProductDetailsRespDto(Product product, String username) {
        return new ProductDetailsRespDto(
                product.getId(),
                username,
                product.getName(),
                String.valueOf(product.getCategory()),
                product.getDescription(),
                product.getQuantity(),
                product.getPrice(),
                product.getCreatedDate(),
                product.getReservationStartTime()
        );
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

    // TODO 동시성 제어 lock 걸기
    @Override
    @Transactional
    public void updateProductQuantity(List<OrderProductUpdateReqDto> orderProductUpdateList) {
        for (OrderProductUpdateReqDto updateDto : orderProductUpdateList) {
            Product findProduct = findProductByIdOrElseThrow(updateDto.getProductId());
            findProduct.decreaseQuantity(updateDto.getQuantity());
        }
    }

    @Override
    public int getProductQuantity(Long productId) {
        Product findProduct = findProductByIdOrElseThrow(productId);
        return findProduct.getQuantity();
    }

    @Override
    @Transactional
    public void updateProductInfo(ProductUpdateReqDto productUpdateReqDto) {
        Product findProduct = findProductByIdOrElseThrow(productUpdateReqDto.getProductId());
        findProduct.updateProduct(productUpdateReqDto);
    }

    @Override
    public Product isAvailableOrder(OrderProductUpdateReqDto orderProductUpdateReqDto) {
        Product findProduct = findProductByIdOrElseThrow(orderProductUpdateReqDto.getProductId());

        if (findProduct.getReservationStartTime().isAfter(LocalDateTime.now())) {
            throw new CommonBadRequestException("failOrder");
        }

        return findProduct;
    }

    @Override
    public void isLeftInStock(OrderProductUpdateReqDto orderProductUpdateReqDto) {
        Long productId = orderProductUpdateReqDto.getProductId();

        Integer leftQuantity = productRedisService.getProductQuantity(productId);
        log.debug("left Quantity>> {}", leftQuantity);
        if (leftQuantity == null) {
            Product findProduct = findProductByIdOrElseThrow(orderProductUpdateReqDto.getProductId());
            int totalQuantity = findProduct.getQuantity();
            productRedisService.saveProduct(productId, totalQuantity, 10, TimeUnit.SECONDS);
            leftQuantity = totalQuantity;
        }

        if (leftQuantity < orderProductUpdateReqDto.getQuantity()) {
            throw new CommonBadRequestException("failOrder");
        }
    }

    @Override
    @Transactional
    public void decreaseQuantity(OrderProductUpdateReqDto orderProductUpdateReqDto) {
        Product findProduct = findProductByIdOrElseThrow(orderProductUpdateReqDto.getProductId());
        findProduct.decreaseQuantity(orderProductUpdateReqDto.getQuantity());

        productRedisService.decreaseProductQuantity(findProduct.getId(), orderProductUpdateReqDto.getQuantity());

        kafkaSender.sendMessage(KafkaVo.KAFKA_PRODUCT_TOPIC, createOrderSaveDto(findProduct, orderProductUpdateReqDto));
    }

    private OrderSaveKafkaReqDto createOrderSaveDto(Product product,
                                                    OrderProductUpdateReqDto orderProductUpdateReqDto) {
        int quantity = orderProductUpdateReqDto.getQuantity();
        return new OrderSaveKafkaReqDto(product.getUserId(), quantity,
                product.getId(), product.getPrice() * quantity);
    }
}
