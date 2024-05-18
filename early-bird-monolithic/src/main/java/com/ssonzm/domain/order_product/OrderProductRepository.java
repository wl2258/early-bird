package com.ssonzm.domain.order_product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findByOrderId(Long orderId);

    @Query("select op from OrderProduct op where op.lastModifiedDate between :prevDay and :nextDay")
    List<OrderProduct> findAllBetweenPrevDayAndToday(@Param("prevDay")LocalDateTime prevDay,
                                                     @Param("nextDay")LocalDateTime nextDay);
}
