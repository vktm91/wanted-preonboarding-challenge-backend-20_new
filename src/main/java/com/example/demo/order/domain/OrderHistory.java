package com.example.demo.order.domain;

import com.example.demo.product.domain.Product;
import com.example.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderHistory {

    private Long id;

    private User buyer;

    private Product product;

    private int price;

    private OrderStatus status;

    @Builder
    public OrderHistory(Long id, User buyer, Product product, int price, OrderStatus status) {
        this.id = id;
        this.buyer = buyer;
        this.product = product;
        this.price = price;
        this.status = status;
    }
//
//    public static OrderHistory from() {
//        return OrderHistory.builder()
//                .buyer()
//                .product()
//                .price()
//    }

    public boolean isHigherOrEqualStep(OrderStatus otherStatus) {
        return this.status.getStep() >= otherStatus.getStep();
    }
}
