package com.example.demo.product.controller.response;

import com.example.demo.order.domain.OrderHistory;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductStatus;
import com.example.demo.user.controller.response.UserResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Builder
public class ProductResponse {

    private final Long id;
    private final String name;
    private final Long price;
    private final ProductStatus status;
    private final Long count;
    private final LocalDateTime registDt;
    private final LocalDateTime updateDt;
    private final UserResponse seller;
    private final List<OrderHistory> sellerOrderHistories;
    private final List<OrderHistory> buyerOrderHistories;

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .status(product.getStatus())
                .count(product.getCount())
                .registDt(product.getRegistDt())
                .updateDt(product.getUpdateDt())
                .seller(UserResponse.from(product.getSeller()))
                .build();
    }

    public static ProductResponse from(Product product, List<OrderHistory> sellerOrderHistories, List<OrderHistory> buyerOrderHistories) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .status(product.getStatus())
                .count(product.getCount())
                .registDt(product.getRegistDt())
                .updateDt(product.getUpdateDt())
                .seller(UserResponse.from(product.getSeller()))
                .sellerOrderHistories(sellerOrderHistories)
                .buyerOrderHistories(buyerOrderHistories)
                .build();
    }
}
