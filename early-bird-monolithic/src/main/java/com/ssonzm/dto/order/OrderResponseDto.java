package com.ssonzm.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssonzm.domain.order.Order;
import com.ssonzm.dto.delivery.DeliveryResponseDto;
import com.ssonzm.dto.order_product.OrderProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {
    @Data
    @AllArgsConstructor
    public static class OrderDetailsRespDto {
        private int totalPrice;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        private LocalDateTime createdDate;
        private List<DeliveryResponseDto.DeliveryDetailsRespDto> deliveryStatus;
        private List<OrderProductResponseDto.OrderProductDetailsRespDto> orderStatus;

        public OrderDetailsRespDto(Order findOrder,
                                   List<DeliveryResponseDto.DeliveryDetailsRespDto> deliveryRespDtos,
                                   List<OrderProductResponseDto.OrderProductDetailsRespDto> orderDetailsRespDtos) {
            this.totalPrice = findOrder.getTotalPrice();
            this.createdDate = findOrder.getCreatedDate();
            this.deliveryStatus = deliveryRespDtos;
            this.orderStatus = orderDetailsRespDtos;
        }
    }
}
