package com.ssonzm.userservcie.service.wish_product;

import com.ssonzm.userservcie.domain.product.Product;
import com.ssonzm.userservcie.domain.wish_product.WishProduct;
import com.ssonzm.userservcie.domain.wish_product.WishProductRepository;
import com.ssonzm.userservcie.dto.wish_product.WishProductRequestDto.WishProductSaveReqDto;
import com.ssonzm.userservcie.service.product.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ssonzm.userservcie.vo.wish_product.WishProductResponseVo.*;

@Service
@Transactional(readOnly = true)
public class WishProductServiceImpl implements WishProductService {
    private final ProductService productService;
    private final WishProductRepository wishProductRepository;

    public WishProductServiceImpl(WishProductRepository wishProductRepository, ProductService productService) {
        this.wishProductRepository = wishProductRepository;
        this.productService = productService;
    }

    @Override
    @Transactional
    public Long saveWishProduct(Long userId, WishProductSaveReqDto wishProductSaveReqDto) {
        Product product = productService.findProductByIdOrElseThrow(wishProductSaveReqDto.getProductId());
        int quantity = wishProductSaveReqDto.getQuantity();
        long productId = product.getId();

        WishProduct wishProduct = wishProductRepository.findByUserIdAndProductId(userId, productId)
                .orElseGet(() -> WishProduct.builder().build());

        if (wishProduct.getId() == null) {
            wishProduct = WishProduct.builder()
                    .productId(product.getId())
                    .userId(userId)
                    .quantity(quantity)
                    .price(quantity * product.getPrice())
                    .build();
            wishProductRepository.save(wishProduct);
        } else {
            updateWishProduct(wishProduct, quantity, product.getPrice());
        }

        return wishProduct.getId();
    }

    private void saveNewWishProduct(Product product, Long userId, int quantity) {
        WishProduct wishProduct = WishProduct.builder()
                .productId(product.getId())
                .userId(userId)
                .quantity(quantity)
                .price(quantity * product.getPrice())
                .build();
        wishProductRepository.save(wishProduct);
    }

    private void updateWishProduct(WishProduct wishProduct, int quantity, int productPrice) {
        wishProduct.addQuantity(quantity);
        wishProduct.addPrice(quantity * productPrice);
    }

    @Override
    public Page<WishProductListRespVo> findWishProductList(Long userId, Pageable pageable) {
        return wishProductRepository.findWishProductList(userId, pageable);
    }
}
