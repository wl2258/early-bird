package com.ssonzm.coremodule.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssonzm.coremodule.dto.delivery.DeliveryResponseDto.DeliveryDetailsRespDto;
import com.ssonzm.coremodule.dto.order_product.OrderProductResponseDto.OrderProductDetailsRespDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {
    @Data
    @AllArgsConstructor
    public static class OrderDetailsRespDto {
        private int totalPrice;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        private LocalDateTime createdDate;
        private List<DeliveryDetailsRespDto> deliveryStatus;
        private List<OrderProductDetailsRespDto> orderStatus;
    }
}
