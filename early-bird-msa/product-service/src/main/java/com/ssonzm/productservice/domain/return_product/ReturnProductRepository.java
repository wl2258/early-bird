package com.ssonzm.productservice.domain.return_product;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReturnProductRepository extends JpaRepository<ReturnProduct, Long> {
    @EntityGraph(attributePaths = {"orderProduct"})
    @Query("select rp from ReturnProduct rp where rp.lastModifiedDate between :prevDay and :nextDay")
    List<ReturnProduct> findAllBetweenPrevDayAndToday(@Param("prevDay") LocalDateTime prevDay,
                                                     @Param("nextDay")LocalDateTime nextDay);
}
