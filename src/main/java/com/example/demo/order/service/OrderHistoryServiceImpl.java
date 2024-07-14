package com.example.demo.order.service;

import com.example.demo.common.domain.exception.OrderStatusBadRequestException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.order.controller.port.OrderHistoryService;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderHistoryUpdate;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.order.service.port.OrderHistoryRepository;
import com.example.demo.product.controller.port.ProductService;
import com.example.demo.product.domain.Product;
import com.example.demo.product.service.port.ProductRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.port.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class OrderHistoryServiceImpl implements OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ClockHolder clockHolder;

    @Override
    @Transactional
    public OrderHistory createOrder(Long buyerId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        User buyer = userRepository.getById(buyerId);

        OrderHistory orderHistory = OrderHistory.from(buyer, product, clockHolder);
        orderHistory.changeProduct(product);
        OrderHistory savedOrderHistory = orderHistoryRepository.save(orderHistory);

//        List<OrderHistory> orderHistories = orderHistoryRepository.findByProduct_Id(productId);
//        Product updatedProduct = product.decreaseCount(clockHolder, orderHistories);
        Product updatedProduct = product.decreaseCount(clockHolder);
        productRepository.save(updatedProduct);

        return savedOrderHistory;
    }

    @Override
    @Transactional
    public OrderHistory updateStatus(OrderHistoryUpdate orderHistoryUpdate) {
        log.info("!!!!! orderHistoryUpdate.getStatusTo(): {}", orderHistoryUpdate.getStatusTo());
        OrderHistory orderHistory = orderHistoryRepository.findById(orderHistoryUpdate.getOrderHistoryId())
                .orElseThrow(() -> new ResourceNotFoundException("OrderHistory", orderHistoryUpdate.getOrderHistoryId()));

        validateStatusTransition(orderHistory.getStatus(), orderHistoryUpdate.getStatusTo());

        OrderHistory updatedOrderHistory = orderHistory.update(orderHistoryUpdate.getStatusTo(), clockHolder);

        orderHistoryRepository.save(updatedOrderHistory);

//        List<OrderHistory> orderHistories = orderHistoryRepository.findByProduct_Id(orderHistory.getProduct().getId());
//        Product updatedProduct = orderHistory.getProduct().updateStatus(clockHolder, orderHistories);

//        Product updatedProduct = orderHistory.getProduct().updateStatus(clockHolder);
        Product product = productRepository.findById(orderHistory.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", orderHistory.getProduct().getId()));
        Product updatedProduct = product.updateStatus(clockHolder);
        productRepository.save(updatedProduct);

        return updatedOrderHistory;
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
