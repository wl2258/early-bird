package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.productservice.domain.product.Product;
import com.ssonzm.productservice.domain.product.ProductCategory;
import com.ssonzm.productservice.domain.product.ProductRepository;
import com.ssonzm.productservice.domain.product.ProductStatus;
import com.ssonzm.productservice.domain.user.User;
import com.ssonzm.productservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssonzm.productservice.dto.product.ProductRequestDto.ProductSaveReqDto;
import static com.ssonzm.productservice.dto.product.ProductResponseDto.ProductDetailsRespDto;
import static com.ssonzm.productservice.vo.product.ProductResponseVo.ProductListRespVo;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final UserService userService;
    private final ProductRepository productRepository;

    public ProductServiceImpl(UserService userService, ProductRepository productRepository) {
        this.userService = userService;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Long saveProduct(Long userId, ProductSaveReqDto productSaveReqDto) {
        User findUser = userService.findByIdOrElseThrow(userId);
        Product product = createProduct(productSaveReqDto, findUser);
        productRepository.save(product);
        return product.getId();
    }

    private Product createProduct(ProductSaveReqDto productSaveReqDto, User findUser) {
        int quantity = productSaveReqDto.getQuantity();
        ProductStatus status = quantity > 0 ? ProductStatus.IN_STOCK : ProductStatus.SOLD_OUT;
        return Product.builder()
                .userId(findUser.getId())
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
        return new ModelMapper().map(findProduct, ProductDetailsRespDto.class);
    }

    @Override
    public Product findProductByIdOrElseThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CommonBadRequestException("notFoundData"));
    }
}
