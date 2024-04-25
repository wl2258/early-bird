package com.ssonzm.orderservice.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssonzm.orderservice.domain.order.Order;
import com.ssonzm.orderservice.dto.delivery.DeliveryResponseDto.DeliveryDetailsRespDto;
import com.ssonzm.orderservice.dto.order_product.OrderProductResponseDto.OrderProductDetailsRespDto;
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
        private List<DeliveryDetailsRespDto> deliveryStatus;
        private List<OrderProductDetailsRespDto> orderStatus;

        public OrderDetailsRespDto(Order findOrder,
                                   List<DeliveryDetailsRespDto> deliveryRespDtos,
                                   List<OrderProductDetailsRespDto> orderDetailsRespDtos) {
            this.totalPrice = findOrder.getTotalPrice();
            this.createdDate = findOrder.getCreatedDate();
            this.deliveryStatus = deliveryRespDtos;
            this.orderStatus = orderDetailsRespDtos;
        }
    }
}
