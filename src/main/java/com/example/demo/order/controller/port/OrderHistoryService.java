package com.example.demo.order.controller.port;

import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderHistoryUpdate;

public interface OrderHistoryService {
    OrderHistory createOrder(Long buyerId, Long productId);
    OrderHistory updateStatus(OrderHistoryUpdate orderHistoryUpdate);
}
