package com.ssonzm.userservcie.service.product;

import com.ssonzm.userservcie.domain.product.Product;
import com.ssonzm.userservcie.domain.product.ProductCategory;
import com.ssonzm.userservcie.domain.product.ProductRepository;
import com.ssonzm.userservcie.domain.product.ProductStatus;
import com.ssonzm.userservcie.domain.user.User;
import com.ssonzm.userservcie.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssonzm.userservcie.dto.product.ProductRequestDto.*;

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

        int quantity = productSaveReqDto.getQuantity();
        Product product = Product.builder()
                .userId(findUser.getId())
                .name(productSaveReqDto.getProductName())
                .category(ProductCategory.valueOf(productSaveReqDto.getCategory()))
                .status(quantity > 0 ? ProductStatus.IN_STOCK : ProductStatus.SOLD_OUT)
                .description(productSaveReqDto.getDescription())
                .price(productSaveReqDto.getPrice())
                .quantity(quantity)
                .build();

        productRepository.save(product);

        return product.getId();
    }
}