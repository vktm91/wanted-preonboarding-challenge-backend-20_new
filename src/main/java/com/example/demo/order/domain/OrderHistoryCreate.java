package com.example.demo.order.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderHistoryCreate {
    @NotNull(message = "ProductId must not be null")
    private final Long productId;
    @NotNull(message = "BuyerId must not be null")
    private final Long buyerId;

    @Builder
    public OrderHistoryCreate(
            @JsonProperty("productId") Long productId,
            @JsonProperty("userId") Long buyerId) {
        this.productId = productId;
        this.buyerId = buyerId;
    }
}
