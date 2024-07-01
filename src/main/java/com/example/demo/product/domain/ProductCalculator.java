package com.example.demo.product.domain;

import com.example.demo.common.domain.exception.NegativeProductCountException;
import com.example.demo.common.domain.exception.ProductStockBadRequestException;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ProductCalculator {
    public static ProductStatus calculateNewStatus(Long newCount, List<OrderHistory> orderHistories) {
        log.info("@@@@@ orderHistories: {}", orderHistories.stream().map(OrderHistory::getStatus).collect(Collectors.toList()));
        if (newCount < 0) {
            throw new NegativeProductCountException(newCount);
        } else if (newCount > 0) {
            return ProductStatus.SALE;
        } else {
            boolean hasNonConfirmed = orderHistories.stream()
                    .anyMatch(orderHistory -> orderHistory.getStatus() != OrderStatus.CONFIRMED);

            return hasNonConfirmed ? ProductStatus.RESERVED : ProductStatus.COMPLETED;
        }
    }

    public static Long decreaseCount(Product product) {
        if (product.getCount() < 1) {
            throw new ProductStockBadRequestException();
        }

        return product.getCount() - 1;
    }
}
