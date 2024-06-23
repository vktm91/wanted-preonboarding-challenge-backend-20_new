package com.example.demo.product.domain;

import com.example.demo.common.domain.exception.NegativeProductCountException;
import com.example.demo.common.domain.exception.ProductStockBadRequestException;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderStatus;

import java.util.List;

public class ProductCalculator {
    public static ProductStatus calculateNewStatus(Long newCount, List<OrderHistory> orderHistories) {
        if (newCount < 0) {
            throw new NegativeProductCountException(newCount);
        }

        if (newCount > 0) {
            return ProductStatus.SALE;
        } else {
            boolean allConfirmed = orderHistories.stream()
                    .allMatch(orderHistory -> orderHistory.isHigherOrEqualStep(OrderStatus.CONFIRMED));

            return allConfirmed ? ProductStatus.COMPLETED : ProductStatus.RESERVED;
        }
    }

    public static Long calculateNewCount(Product product) {
        if (product.getCount() < 1) {
            throw new ProductStockBadRequestException();
        }

        return product.getCount() - 1;
    }
}
