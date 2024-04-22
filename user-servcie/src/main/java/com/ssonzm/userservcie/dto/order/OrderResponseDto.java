package com.ssonzm.userservcie.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssonzm.userservcie.dto.delivery.DeliveryResponseDto.DeliveryDetailsRespDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {
    @Data
    @Builder
    public static class OrderDetailsRespDto {
        private String orderStatus;
        private List<DeliveryDetailsRespDto> deliveryStatus;
        private int totalPrice;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        private LocalDateTime createdDate;
    }
}
