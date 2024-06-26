package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.domain.ImageFile;
import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.ProductUpdateAfterOrderReqDto;
import com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductUpdateReqDto;
import com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsFeignClientRespDto;
import com.ssonzm.coremodule.dto.property.CloudFrontProperties;
import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.coremodule.vo.FileRootPathVO;
import com.ssonzm.productservice.domain.product.Product;
import com.ssonzm.productservice.domain.product.ProductCategory;
import com.ssonzm.productservice.domain.product.ProductRepository;
import com.ssonzm.productservice.domain.product.ProductStatus;
import com.ssonzm.productservice.common.util.CloudFrontUtil;
import com.ssonzm.productservice.service.aws_s3.S3Service;
import com.ssonzm.productservice.service.client.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductSaveReqDto;
import static com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsRespDto;
import static com.ssonzm.coremodule.dto.user.UserResponseDto.UserDetailsDto;
import static com.ssonzm.coremodule.vo.product.ProductResponseVo.ProductListRespVo;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final S3Service s3Service;
    private final ProductRepository productRepository;
    private final UserServiceClient userServiceClient;
    private final ProductRedisService productRedisService;
    private final CloudFrontProperties cloudFrontProperties;

    public ProductServiceImpl(S3Service s3Service, ProductRepository productRepository, UserServiceClient userServiceClient,
                              ProductRedisService productRedisService, CloudFrontProperties cloudFrontProperties) {
        this.s3Service = s3Service;
        this.productRepository = productRepository;
        this.userServiceClient = userServiceClient;
        this.productRedisService = productRedisService;
        this.cloudFrontProperties = cloudFrontProperties;
    }

    /**
     * 엔티티만 저장
     */
    @Override
    @Transactional
    public Product saveProduct(Long userId, ProductSaveReqDto productSaveReqDto) {
        Product product = createProduct(productSaveReqDto, userId);
        productRepository.save(product);

        productRedisService.saveProduct(product.getId(), product.getQuantity(), 1, TimeUnit.HOURS);
        return product;
    }

    /**
     * 이미지, 엔티티 같이 저장
     */
    @Transactional
    public void saveProduct(Long userId, ProductSaveReqDto productSaveReqDto, MultipartFile file) {
        Product product = saveProduct(userId, productSaveReqDto);

        ImageFile imageFile = null;
        try {
            imageFile = saveFile(file);
        } catch (IOException e) {
            log.error("AWS S3 파일 업로드 실패");
            throw new RuntimeException(e);
        }
        product.uploadImageFile(imageFile);

        productRepository.save(product);
    }

    private ImageFile saveFile(MultipartFile file) throws IOException {
        return s3Service.upload(file, FileRootPathVO.PRODUCT_PATH);
    }

    private Product createProduct(ProductSaveReqDto productSaveReqDto, Long userId) {
        int quantity = productSaveReqDto.getQuantity();
        ProductStatus status = quantity > 0 ? ProductStatus.IN_STOCK : ProductStatus.SOLD_OUT;
        return createProduct(productSaveReqDto, userId, status, quantity);
    }

    private static Product createProduct(ProductSaveReqDto productSaveReqDto, Long userId, ProductStatus status, int quantity) {
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
                product.getReservationStartTime(),
                s3Service.getS3ImageUrl(product.getImageFile().getStoreFileName())
//                product.getImageFile().getStoreFileUrl()
        );
    }

    @Override
    public List<ProductDetailsFeignClientRespDto> getProductDetailsByIds(List<Long> productIds) {
        List<Product> productList = productRepository.findAllByIds(productIds);
        return productList.stream()
                .map(this::mapToProductDetailRespDto)
                .toList();
    }

    public ProductDetailsFeignClientRespDto mapToProductDetailRespDto(Product product) {
        return new ProductDetailsFeignClientRespDto(
                product.getId(),
                product.getPrice(),
                product.getName(),
                product.getReservationStartTime()
        );
    }

    @Override
    public Product findProductByIdOrElseThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->   new CommonBadRequestException("notFoundData"));
    }

    @Override
    public Page<ProductListRespVo> getProductSavedByUser(Pageable pageable, Long userId) {
        return productRepository.getProductListByUser(pageable, userId);
    }

    @Override
    @Transactional
    public void updateProductQuantity(List<ProductUpdateAfterOrderReqDto> orderProductUpdateList) {
        for (ProductUpdateAfterOrderReqDto updateDto : orderProductUpdateList) {
            Product findProduct = findProductByIdOrElseThrow(updateDto.getProductId());
            findProduct.decreaseQuantity(updateDto.getQuantity());
        }
    }

    @Override
    public int getProductQuantity(Long productId) {
        Integer productQuantity = productRedisService.getProductQuantity(productId);
        if (productQuantity == null) {
            Product findProduct = findProductByIdOrElseThrow(productId);
            productQuantity = findProduct.getQuantity();
        }

        return productQuantity;
    }

    @Override
    @Transactional
    public void updateProductInfo(ProductUpdateReqDto productUpdateReqDto) {
        Product findProduct = findProductByIdOrElseThrow(productUpdateReqDto.getProductId());
        findProduct.updateProduct(productUpdateReqDto);
    }

    @Override
    public void isAvailableOrder(OrderProductUpdateReqDto orderProductUpdateReqDto) {
        Product findProduct = findProductByIdOrElseThrow(orderProductUpdateReqDto.getProductId());

        if (findProduct.getReservationStartTime().isAfter(LocalDateTime.now())) {
            throw new CommonBadRequestException("failOrder");
        }
    }

    @Override
    public void isLeftInStock(OrderProductUpdateReqDto orderProductUpdateReqDto) {
        Long productId = orderProductUpdateReqDto.getProductId();

        Integer leftQuantity = productRedisService.getProductQuantity(productId);
        if (leftQuantity == null) {
            Product findProduct = findProductByIdOrElseThrow(orderProductUpdateReqDto.getProductId());
            int totalQuantity = findProduct.getQuantity();
            productRedisService.saveProduct(productId, totalQuantity, 1, TimeUnit.HOURS);
            leftQuantity = totalQuantity;
        }

        if (leftQuantity < orderProductUpdateReqDto.getQuantity()) {
            throw new CommonBadRequestException("outOfStock");
        }
    }

    @Override
    @Transactional
    public void decreaseQuantity(Long userId, OrderProductUpdateReqDto orderProductUpdateReqDto) {
        // write-through
        Product findProduct = findProductByIdOrElseThrow(orderProductUpdateReqDto.getProductId());
        findProduct.decreaseQuantity(orderProductUpdateReqDto.getQuantity());

        productRedisService.decreaseProductQuantity(orderProductUpdateReqDto.getProductId(), orderProductUpdateReqDto.getQuantity());
    }

    @Override
    public void increaseQuantity(Long productId, int quantity) {
        // write-through
        Product product = findProductByIdOrElseThrow(productId);
        product.increaseQuantity(quantity);

        Integer totalQuantity = productRedisService.getProductQuantity(productId);

        // 재고가 존재하지 않는 경우 레디스에 저장
        if (totalQuantity == null) {
            // write-back (not used lua)
//            Product product = findProductByIdOrElseThrow(productId);
            productRedisService.saveProduct(product.getId(), product.getQuantity(), 1, TimeUnit.HOURS);
        } else {
            productRedisService.increaseProductQuantity(productId,  quantity);
        }
    }

    /**
     * 루아 스크립트 적용
     */
    @Override
    public Long getProductQuantityByLua(Long productId) {
        return productRedisService.getQuantityInRedisByLua(productId);
    }

    @Override
    public void decreaseQuantityByLua(OrderProductUpdateReqDto orderProductUpdateReqDto) {
        productRedisService.decreaseQuantityInRedisByLua(orderProductUpdateReqDto);
    }

    @Override
    public void increaseQuantityByLua(Long productId, Integer quantity) {
        productRedisService.increaseQuantityInRedisByLua(productId, quantity);
    }
}