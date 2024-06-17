package com.example.demo.product.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
public class ProductUpdate {
    private String productNm;
    private Long productPrice;
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