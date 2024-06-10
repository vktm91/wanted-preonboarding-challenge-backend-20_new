package com.example.demo.product.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
public class ProductUpdate {
    private Optional<@NotNull String> productNm;
    private Optional<@Min(0) Long> productPrice;
    private Optional<@Min(0) Long> count;

    @Builder
    public ProductUpdate(
            @JsonProperty("productNm") Optional<String> productNm,
            @JsonProperty("productPrice") Optional<Long> productPrice,
            @JsonProperty("count") Optional<Long> count) {
        this.productNm = productNm;
        this.productPrice = productPrice;
        this.count = count;
    }
}