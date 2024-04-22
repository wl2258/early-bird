package com.ssonzm.userservcie.service.return_product;

import com.ssonzm.userservcie.common.exception.CommonBadRequestException;
import com.ssonzm.userservcie.domain.delivery.Delivery;
import com.ssonzm.userservcie.domain.delivery.DeliveryStatus;
import com.ssonzm.userservcie.domain.order_product.OrderProduct;
import com.ssonzm.userservcie.domain.product.Product;
import com.ssonzm.userservcie.domain.return_product.ReturnProduct;
import com.ssonzm.userservcie.domain.return_product.ReturnProductRepository;
import com.ssonzm.userservcie.domain.return_product.ReturnStatus;
import com.ssonzm.userservcie.service.delivery.DeliveryService;
import com.ssonzm.userservcie.service.order_product.OrderProductService;
import com.ssonzm.userservcie.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.ssonzm.userservcie.dto.return_product.ReturnProductRequestDto.ReturnProductSaveReqDto;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ReturnProductServiceImpl implements ReturnProductService {
    private final ProductService productService;
    private final DeliveryService deliveryService;
    private final OrderProductService orderProductService;
    private final ReturnProductRepository returnProductRepository;

    public ReturnProductServiceImpl(ProductService productService, DeliveryService deliveryService, OrderProductService orderProductService,
                                    ReturnProductRepository returnProductRepository) {
        this.productService = productService;
        this.deliveryService = deliveryService;
        this.orderProductService = orderProductService;
        this.returnProductRepository = returnProductRepository;
    }

    @Override
    @Transactional
    public Long saveReturn(Long userId, ReturnProductSaveReqDto returnProductSaveReqDto) {
        OrderProduct findOrderProduct =
                orderProductService.findOrderProductByIdOrElseThrow(returnProductSaveReqDto.getOrderProductId());

        Delivery findDelivery = deliveryService.findDeliveryByOrderProductIdOrElseThrow(findOrderProduct.getId());
        LocalDateTime returnsPossibleDate = findDelivery.getLastModifiedDate().plusDays(1);

        if (equalsDeliveryStatus(findDelivery) && returnsPossibleDate.isAfter(LocalDateTime.now())) {
            ReturnProduct returnProduct = createReturnProduct(userId, returnProductSaveReqDto, findOrderProduct);
            return returnProductRepository.save(returnProduct).getId();
        } else {
            throw new CommonBadRequestException("failReturn");
        }
    }

    private static boolean equalsDeliveryStatus(Delivery delivery) {
        return delivery.getStatus().equals(DeliveryStatus.DELIVERED);
    }

    @Override
    @Transactional
    public void updateReturnStatus() {
        log.info("Update Return Status to APPROVED");
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime yesterday = today.minusDays(1);

        List<ReturnProduct> returnProductList = returnProductRepository.findAllBetweenPrevDayAndToday(yesterday, today);
        returnProductList.forEach(this::restoreProductQuantity);
    }

    private void restoreProductQuantity(ReturnProduct returnProduct) {
        OrderProduct orderProduct = returnProduct.getOrderProduct();

        Product findProduct = productService.findProductByIdOrElseThrow(orderProduct.getProductId());
        findProduct.updateQuantity(orderProduct.getQuantity());

        returnProduct.updateStatus(ReturnStatus.APPROVED);
    }

    private static ReturnProduct createReturnProduct(Long userId, ReturnProductSaveReqDto returnProductSaveReqDto, OrderProduct orderProduct) {
        return ReturnProduct.builder()
                .userId(userId)
                .reason(returnProductSaveReqDto.getReason())
                .orderProduct(orderProduct)
                .status(ReturnStatus.REQUESTED)
                .build();
    }
}
