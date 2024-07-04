package com.example.demo.order.controller.response;

import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
public class OrderHistoriesResponse {
    private final Long id;
    private final Long buyerId;
    private final Long productId;
    private final Long price;
    private final OrderStatus status;
    private final LocalDateTime registDt;
    private final LocalDateTime updateDt;

    public static OrderHistoriesResponse from(OrderHistory orderHistory) {
        return OrderHistoriesResponse.builder()
                .id(orderHistory.getId())
                .buyerId(orderHistory.getBuyer().getId())
                .productId(orderHistory.getProduct().getId())
                .price(orderHistory.getPrice())
                .status(orderHistory.getStatus())
                .registDt(orderHistory.getRegistDt())
                .updateDt(orderHistory.getUpdateDt())
                .build();
    }
}
