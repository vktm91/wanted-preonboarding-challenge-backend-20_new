package com.example.demo.product.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductUpdate {
    private String productNm;

    @Min(value = 1, message = "Product price must be at least 1")
    private Long productPrice;

    @Min(value = 0, message = "Product count must be at least 0")
    private Long count;

    @Builder
    public ProductUpdate(
            @JsonProperty("productNm") String productNm,
            @JsonProperty("productPrice") Long productPrice,
            @JsonProperty("count") Long count) {
        this.productNm = productNm;
        this.productPrice = productPrice;
        this.count = count;
    }
}