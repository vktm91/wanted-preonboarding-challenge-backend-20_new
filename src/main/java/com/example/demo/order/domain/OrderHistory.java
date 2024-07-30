package com.example.demo.order.domain;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.product.domain.Product;
import com.example.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderHistory that = (OrderHistory) o;

        boolean equals = Objects.equals(id, that.id);

        log.info("?????????????? equals: {}", equals);

        return equals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

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

    public OrderHistory updateStatus(OrderStatus orderStatus, ClockHolder clockHolder) {
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
