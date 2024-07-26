package com.example.demo.order.service;

import com.example.demo.common.domain.exception.OrderStatusBadRequestException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.order.controller.port.OrderHistoryReadService;
import com.example.demo.order.controller.port.OrderHistoryService;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderHistoryUpdate;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.order.service.port.OrderHistoryRepository;
import com.example.demo.product.controller.port.ProductService;
import com.example.demo.product.domain.Product;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.port.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class OrderHistoryServiceImpl implements OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderHistoryReadService orderHistoryReadService;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;

    @Override
    @Transactional
    public OrderHistory createOrder(Long buyerId, Long productId) {
        Product product = productService.getById(productId);
        User buyer = userRepository.getById(buyerId);

        OrderHistory orderHistory = OrderHistory.from(buyer, product, clockHolder);
        orderHistory = orderHistoryRepository.save(orderHistory);
        orderHistory.changeProduct(product);

        List<OrderHistory> orderHistories = product.getOrderHistories();

        product = productService.decreaseCount(product.getId(), orderHistories);

        productService.save(product);

        return orderHistory;
    }

    @Override
    @Transactional
    public OrderHistory updateStatus(OrderHistoryUpdate orderHistoryUpdate) {
        OrderHistory orderHistory = orderHistoryRepository.findById(orderHistoryUpdate.getOrderHistoryId())
                .orElseThrow(() -> new ResourceNotFoundException("OrderHistory", orderHistoryUpdate.getOrderHistoryId()));

        validateStatusTransition(orderHistory.getStatus(), orderHistoryUpdate.getStatusTo());

//        orderHistory.update(orderHistoryUpdate.getStatusTo(), clockHolder);
        orderHistory = orderHistory.update(orderHistoryUpdate.getStatusTo(), clockHolder);

        Product updatedProduct = productService.updateStatus(orderHistory.getProduct().getId());
//        orderHistory.changeProduct(updatedProduct);

        orderHistoryRepository.save(orderHistory);

        return orderHistory;
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
