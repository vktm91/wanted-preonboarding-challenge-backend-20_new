package com.example.demo.product.domain;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@ToString
@Getter
public class Product {
    private final Long id;
    private final String name;
    private final Long price;
    private final ProductStatus status;
    private final Long count;
    private final LocalDateTime registDt;
    private final LocalDateTime updateDt;
    private final User seller;
    @ToString.Exclude
    private final List<OrderHistory> orderHistories;

    @Builder
    public Product(Long id, String name, Long price, ProductStatus status, Long count, LocalDateTime registDt, LocalDateTime updateDt, User seller, List<OrderHistory> orderHistories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.status = status;
        this.count = count;
        this.registDt = registDt;
        this.updateDt = updateDt;
        this.seller = seller;
        this.orderHistories = orderHistories != null ? orderHistories : new ArrayList<>();
    }

    public static Product from(User seller, ProductCreate productCreate, ClockHolder clockHolder) {
        return Product.builder()
                .name(productCreate.getProductNm())
                .price(productCreate.getProductPrice())
                .status(ProductStatus.SALE)
                .count(productCreate.getCount())
                .registDt(clockHolder.getNowDt())
                .seller(seller)
                .build();
    }

    public Product updateInfo(ProductUpdate productUpdate, ClockHolder clockHolder, List<OrderHistory> orderHistories) {
        ProductStatus newStatus = Objects.nonNull(productUpdate.getCount()) ?
                ProductCalculator.calculateNewStatus(productUpdate.getCount(), orderHistories) : status;

        return Product.builder()
                .id(id)
                .name(StringUtils.isNotBlank(productUpdate.getProductNm()) ? productUpdate.getProductNm() : name)
                .price(Objects.nonNull(productUpdate.getProductPrice()) ? productUpdate.getProductPrice() : price)
                .status(newStatus)
                .count(Objects.nonNull(productUpdate.getCount()) ? productUpdate.getCount() : count)
                .registDt(registDt)
                .updateDt(clockHolder.getNowDt())
                .seller(seller)
                .orderHistories(orderHistories)
                .build();
    }

    public Product updateStatus(ClockHolder clockHolder, List<OrderHistory> orderHistories) {
        ProductStatus newStatus = ProductCalculator.calculateNewStatus(count, orderHistories);

        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .status(newStatus)
                .count(count)
                .registDt(registDt)
                .updateDt(clockHolder.getNowDt())
                .seller(seller)
                .orderHistories(orderHistories)
                .build();
    }

    public Product decreaseCount(ClockHolder clockHolder, List<OrderHistory> orderHistories) {
        Long newCount = ProductCalculator.decreaseCount(this);

        ProductStatus newStatus = ProductCalculator.calculateNewStatus(newCount, orderHistories);

        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .status(newStatus)
                .count(newCount)
                .registDt(registDt)
                .updateDt(clockHolder.getNowDt())
                .seller(seller)
                .orderHistories(orderHistories)
                .build();
    }
}
