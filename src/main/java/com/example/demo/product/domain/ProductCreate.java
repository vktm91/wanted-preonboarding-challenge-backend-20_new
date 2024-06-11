package com.example.demo.product.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductCreate {
    @NotBlank(message = "Product name must not be blank")
    private final String productNm;

    @NotNull(message = "Product price must not be null")
    @Min(value = 1, message = "Product price must be at least 1")
    private final Long productPrice;

    @NotNull(message = "Count must not be null")
    @Min(value = 1, message = "Count must be at least 1")
    private final Long count;

    @NotNull(message = "Seller ID must not be null")
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
