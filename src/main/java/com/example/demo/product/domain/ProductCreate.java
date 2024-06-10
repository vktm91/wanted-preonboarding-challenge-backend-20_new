package com.example.demo.product.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductCreate {
    private final String productNm;
    private final Long productPrice;
    private final Long count;
    private final Long sellerId;

    @Builder
    public ProductCreate(
            @JsonProperty("productNm") String productNm,
            @JsonProperty("productPrice") Long productPrice,
            @JsonProperty("count") Long count,
            @JsonProperty("sellerId") Long sellerId) {
        this.productNm = productNm;
        this.productPrice = productPrice;
        this.count = count;
        this.sellerId = sellerId;
    }
}
