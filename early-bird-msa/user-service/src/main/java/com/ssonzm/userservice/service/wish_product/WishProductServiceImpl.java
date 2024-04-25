package com.ssonzm.userservice.service.wish_product;

import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.userservice.domain.product.Product;
import com.ssonzm.userservice.domain.wish_product.WishProduct;
import com.ssonzm.userservice.domain.wish_product.WishProductRepository;
import com.ssonzm.userservice.dto.wish_product.WishProductRequestDto.WishProductSaveReqDto;
import com.ssonzm.userservice.dto.wish_product.WishProductRequestDto.WishProductUpdateReqDto;
import com.ssonzm.userservice.service.product.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssonzm.userservice.vo.wish_product.WishProductResponseVo.WishProductListRespVo;

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
                .map(wp -> updateWishProduct(wp, wp.getQuantity() + quantity, wp.getPrice() + (quantity * product.getPrice())))
                .orElseGet(() -> saveWishProduct(userId, product, quantity, quantity * product.getPrice()));

        return wishProduct.getId();
    }
    private WishProduct saveWishProduct(Long userId, Product product, int quantity, int price) {
        WishProduct wishProduct = WishProduct.builder()
                .product(product)
                .userId(userId)
                .quantity(quantity)
                .price(quantity * price)
                .build();

        return wishProductRepository.save(wishProduct);
    }

    private WishProduct updateWishProduct(WishProduct wishProduct, int quantity, int price) {
        wishProduct.updateQuantityAndPrice(quantity, price);
        return wishProduct;
    }

    @Override
    public Page<WishProductListRespVo> findWishProductList(Long userId, Pageable pageable) {
        return wishProductRepository.findWishProductList(userId, pageable);
    }

    @Override
    @Transactional
    public void updateQuantity(WishProductUpdateReqDto wishProductUpdateReqDto) {
        WishProduct findWishProduct = findWishProductOrThrow(wishProductUpdateReqDto.getWishProductId());
        Product product = findWishProduct.getProduct();

        int quantity = wishProductUpdateReqDto.getQuantity();
        updateWishProduct(findWishProduct, quantity, quantity * product.getPrice());
    }

    @Override
    public WishProduct findWishProductOrThrow(Long wishProductId) {
        return wishProductRepository.findByIdProductFetchJoin(wishProductId)
                .orElseThrow(() -> new CommonBadRequestException("notFoundProduct"));
    }

    @Override
    @Transactional
    public void deleteWishProduct(Long wishProductId) {
        WishProduct findWishProduct = findWishProductOrThrow(wishProductId);
        wishProductRepository.delete(findWishProduct);
    }
}
