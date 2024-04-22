package com.ssonzm.userservcie.service.order_product;

import com.ssonzm.userservcie.common.exception.CommonBadRequestException;
import com.ssonzm.userservcie.domain.order_product.OrderProduct;
import com.ssonzm.userservcie.domain.order_product.OrderProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderProductServiceImpl implements OrderProductService {
    private final OrderProductRepository orderProductRepository;

    public OrderProductServiceImpl(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public List<OrderProduct> findAllByOrderIdOrElseThrow(Long orderId) {
        return orderProductRepository.findByOrderId(orderId);
    }

    @Override
    public OrderProduct findOrderProductByIdOrElseThrow(Long orderProductId) {
        return orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> new CommonBadRequestException("notFoundData"));
    }

    @Override
    public List<OrderProduct> findAllBetweenYesterdayAndToday() {
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime yesterday = today.minusDays(1);

        return orderProductRepository.findAllBetweenPrevDayAndToday(yesterday, today);
    }

    @Override
    public List<OrderProduct> findAllBetweenTwoDaysAgoAndYesterday() {
        LocalDateTime yesterday = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime twoDaysAgo = yesterday.minusDays(1);

        return orderProductRepository.findAllBetweenPrevDayAndToday(twoDaysAgo, yesterday);
    }
}
