package com.ssonzm.userservcie.service.wish_product;

import com.ssonzm.userservcie.domain.product.Product;
import com.ssonzm.userservcie.domain.wish_product.WishProduct;
import com.ssonzm.userservcie.domain.wish_product.WishProductRepository;
import com.ssonzm.userservcie.dto.wish_product.WishProductRequestDto.WishProductSaveReqDto;
import com.ssonzm.userservcie.service.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WishProductServiceImpl implements WishProductService {
    private final WishProductRepository wishProductRepository;
    private final ProductService productService;

    public WishProductServiceImpl(WishProductRepository wishProductRepository, ProductService productService) {
        this.wishProductRepository = wishProductRepository;
        this.productService = productService;
    }

    @Override
    @Transactional
    public void saveWishProduct(Long userId, WishProductSaveReqDto wishProductSaveReqDto) {
        Product product = productService.findProductByIdOrElseThrow(wishProductSaveReqDto.getProductId());

        WishProduct wishProduct = WishProduct.builder()
                .productId(product.getId())
                .userId(userId)
                .quantity(wishProductSaveReqDto.getProductQuantity())
                .build();

        wishProductRepository.save(wishProduct);
    }
}
