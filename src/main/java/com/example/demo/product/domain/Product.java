package com.example.demo.product.domain;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
        this.orderHistories = orderHistories;
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

    public Product update(ProductUpdate productUpdate, List<OrderHistory> orderHistories, ClockHolder clockHolder) {
        ProductStatus newStatus = Objects.nonNull(productUpdate.getCount()) ?
                ProductCalculator.calculateNewStatus(productUpdate.getCount(), orderHistories) : this.status;

        return Product.builder()
                .id(this.id)
                .name(StringUtils.isNotBlank(productUpdate.getProductNm()) ? productUpdate.getProductNm() : this.name)
                .price(Objects.nonNull(productUpdate.getProductPrice()) ? productUpdate.getProductPrice() : this.price)
                .status(newStatus)
                .count(Objects.nonNull(productUpdate.getCount()) ? productUpdate.getCount() :this.count)
                .registDt(this.registDt)
                .updateDt(clockHolder.getNowDt())
                .seller(this.seller)
                .build();
    }

    public Product decreaseCount(ClockHolder clockHolder) {
        Long newCount = ProductCalculator.calculateNewCount(this);

        ProductStatus newStatus = ProductCalculator.calculateNewStatus(newCount, this.orderHistories);

        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .status(newStatus)
                .count(newCount)
                .registDt(registDt)
                .updateDt(clockHolder.getNowDt())
                .seller(seller)
                .build();
    }
}
