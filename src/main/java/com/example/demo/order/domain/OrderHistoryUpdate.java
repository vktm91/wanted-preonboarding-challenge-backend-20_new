package com.example.demo.order.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderHistoryUpdate {
    private final long orderHistoryId;
    private final OrderStatus statusTo;

    @Builder
    public OrderHistoryUpdate(
            @JsonProperty("orderHistoryId") long orderHistoryId,
            @JsonProperty("status") OrderStatus statusTo) {
        this.orderHistoryId = orderHistoryId;
        this.statusTo = statusTo;
    }
}
