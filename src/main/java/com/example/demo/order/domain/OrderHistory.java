package com.example.demo.order.domain;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.product.domain.Product;
import com.example.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class OrderHistory {

    private final Long id;
    private final User buyer;
    private final Long price;
    private final LocalDateTime registDt;
    @ToString.Exclude
    private Product product;
    private OrderStatus status;
    private LocalDateTime updateDt;

    @Builder
    public OrderHistory(Long id, User buyer, Product product, Long price, OrderStatus status, LocalDateTime registDt, LocalDateTime updateDt) {
        this.id = id;
        this.buyer = buyer;
        this.product = product;
        this.price = price;
        this.status = status;
        this.registDt = registDt;
        this.updateDt = updateDt;
    }

    public void changeProduct(Product newProduct) {
        if (this.product != null) {
            this.product.getOrderHistories().remove(this);
        }

        this.product = newProduct;
        newProduct.getOrderHistories().add(this);
    }

    public static OrderHistory from(User buyer, Product product, ClockHolder clockHolder) {
        return OrderHistory.builder()
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(clockHolder.getNowDt())
                .build();
    }
//
//    public void update(OrderStatus orderStatus, ClockHolder clockHolder) {
//        this.status = orderStatus;
//        this.updateDt = clockHolder.getNowDt();
//    }

    public OrderHistory update(OrderStatus orderStatus, ClockHolder clockHolder) {
        return OrderHistory.builder()
                .id(this.id)
                .buyer(this.buyer)
                .product(this.product)
                .price(this.price)
                .status(orderStatus)
                .registDt(this.registDt)
                .updateDt(clockHolder.getNowDt())
                .build();
    }
}
