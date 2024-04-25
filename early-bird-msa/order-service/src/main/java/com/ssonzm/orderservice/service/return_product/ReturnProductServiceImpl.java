package com.ssonzm.orderservice.service.return_product;

import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.orderservice.domain.delivery.Delivery;
import com.ssonzm.orderservice.domain.delivery.DeliveryStatus;
import com.ssonzm.orderservice.domain.order_product.OrderProduct;
import com.ssonzm.orderservice.domain.return_product.ReturnProduct;
import com.ssonzm.orderservice.domain.return_product.ReturnProductRepository;
import com.ssonzm.orderservice.domain.return_product.ReturnStatus;
import com.ssonzm.orderservice.service.delivery.DeliveryService;
import com.ssonzm.orderservice.service.order_product.OrderProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.ssonzm.orderservice.dto.return_product.ReturnProductRequestDto.ReturnProductSaveReqDto;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ReturnProductServiceImpl implements ReturnProductService {
    private final DeliveryService deliveryService;
    private final OrderProductService orderProductService;
    private final ReturnProductRepository returnProductRepository;

    public ReturnProductServiceImpl(DeliveryService deliveryService, OrderProductService orderProductService,
                                    ReturnProductRepository returnProductRepository) {
        this.deliveryService = deliveryService;
        this.orderProductService = orderProductService;
        this.returnProductRepository = returnProductRepository;
    }

    @Override
    @Transactional
    public Long saveReturn(Long userId, ReturnProductSaveReqDto returnProductSaveReqDto) {
        OrderProduct findOrderProduct = orderProductService.findOrderProductByIdOrElseThrow(returnProductSaveReqDto.getOrderProductId());
        Delivery findDelivery = deliveryService.findDeliveryByOrderProductIdOrElseThrow(findOrderProduct.getId());

        if (isDeliveryStatusValid(findDelivery) && isReturnPossible(findDelivery)) {
            ReturnProduct returnProduct = createReturnProduct(userId, returnProductSaveReqDto, findOrderProduct);
            return returnProductRepository.save(returnProduct).getId();
        }
        throw new CommonBadRequestException("failReturn");
    }

    private boolean isDeliveryStatusValid(Delivery delivery) {
        return delivery.getStatus().equals(DeliveryStatus.DELIVERED);
    }

    private boolean isReturnPossible(Delivery delivery) {
        LocalDateTime returnsPossibleDate = delivery.getLastModifiedDate().plusDays(1);
        return returnsPossibleDate.isAfter(LocalDateTime.now());
    }

    private ReturnProduct createReturnProduct(Long userId, ReturnProductSaveReqDto returnProductSaveReqDto, OrderProduct orderProduct) {
        return ReturnProduct.builder()
                .userId(userId)
                .reason(returnProductSaveReqDto.getReason())
                .orderProduct(orderProduct)
                .status(ReturnStatus.REQUESTED)
                .build();
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
        // TODO 수정 필요
/*        Product findProduct = productService.findProductByIdOrElseThrow(orderProduct.getProductId());
        findProduct.updateQuantity(orderProduct.getQuantity());*/
        returnProduct.updateStatus(ReturnStatus.APPROVED);
    }
}