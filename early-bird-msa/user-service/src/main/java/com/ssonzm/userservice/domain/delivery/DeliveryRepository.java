package com.ssonzm.userservice.domain.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findDeliveryByOrderProductId(Long orderProductId);

    @Query("select d from Delivery d where d.orderProductId in :orderProductIds")
    List<Delivery>  findDeliveryByOrderProductIds(@Param("orderProductIds")List<Long> orderProductIds);
}
