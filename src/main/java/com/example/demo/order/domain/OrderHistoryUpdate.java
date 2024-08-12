package com.example.demo.order.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderHistoryUpdate {
    @NotNull(message = "orderHistoryId must not be null")
    private final Long orderHistoryId;
    @NotNull(message = "statusTo must not be null")
    private final OrderStatus statusTo;

    @Builder
    public OrderHistoryUpdate(
            @JsonProperty("orderHistoryId") Long orderHistoryId,
            @JsonProperty("status") OrderStatus statusTo) {
        this.orderHistoryId = orderHistoryId;
        this.statusTo = statusTo;
    }
}
