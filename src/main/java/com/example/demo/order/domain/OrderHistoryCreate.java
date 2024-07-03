package com.example.demo.order.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderHistoryCreate {
    private final long productId;
    private final long buyerId;

    @Builder
    public OrderHistoryCreate(
            @JsonProperty("productId") long productId,
            @JsonProperty("userId") long buyerId) {
        this.productId = productId;
        this.buyerId = buyerId;
    }
}
