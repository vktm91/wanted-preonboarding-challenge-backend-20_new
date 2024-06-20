package com.example.demo.order.service;

import com.example.demo.common.domain.exception.OrderStatusBadRequestException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.order.controller.port.OrderHistoryService;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderHistoryUpdate;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.order.service.port.OrderHistoryRepository;
import com.example.demo.product.domain.Product;
import com.example.demo.product.service.port.ProductRepository;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Builder
@RequiredArgsConstructor
public class OrderHistoryServiceImpl implements OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ClockHolder clockHolder;

    @Override
    public List<OrderHistory> getOrderHistoriesByProductNo(long productId) {
        return orderHistoryRepository.findByProduct_Id(productId);
    }

    @Override
    public List<OrderHistory> getOrderHistoriesByBuyerId(long buyerId) {
        return orderHistoryRepository.findByBuyerId(buyerId);
    }

    @Override
    public List<OrderHistory> getOrderHistoriesBySellerId(long sellerId) {
        return orderHistoryRepository.findBySellerId(sellerId);
    }

    @Override
    public List<OrderHistory> getOrderHistoriesByProductIdAndBuyerId(long productId, long buyerId) {
        return orderHistoryRepository.findByProduct_IdAndBuyer_Id(productId, buyerId);
    }

    @Override
    public List<OrderHistory> getOrderHistoriesByPorductIdAndSellerId(long productId, long sellerId) {
        return orderHistoryRepository.findByProduct_IdAndProduct_Seller_Id(productId, sellerId);
    }

    @Override
    public OrderHistory updateStatus(OrderHistoryUpdate orderHistoryUpdate) {
        OrderHistory orderHistory = orderHistoryRepository.findById(orderHistoryUpdate.getOrderHistoryId())
                .orElseThrow(() -> new ResourceNotFoundException("OrderHistory", orderHistoryUpdate.getOrderHistoryId()));

        validateStatusTransition(orderHistory.getStatus(), orderHistoryUpdate.getStatusTo());

        orderHistory.update(orderHistoryUpdate.getStatusTo(), clockHolder);

        orderHistoryRepository.save(orderHistory);

        Product product = productRepository.findById(orderHistory.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", orderHistory.getProduct().getId()));

        product.getO

    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (newStatus == OrderStatus.APPROVED) {
            if (currentStatus != OrderStatus.RESERVED) {
                throw new OrderStatusBadRequestException(currentStatus, newStatus);
            }
        } else if (newStatus == OrderStatus.CONFIRMED) {
            if (currentStatus != OrderStatus.APPROVED) {
                throw new OrderStatusBadRequestException(currentStatus, newStatus);
            }
        } else {
            throw new OrderStatusBadRequestException();
        }
    }
}
