package com.example.demo.order.service.port;

import com.example.demo.order.domain.OrderHistory;

import java.util.List;
import java.util.Optional;

public interface OrderHistoryRepository {
    Optional<OrderHistory> findById(long id);
    OrderHistory save(OrderHistory orderHistory);
    List<OrderHistory> findByProduct_Id(long productId);
    List<OrderHistory> findByBuyerId(long buyerId);
    List<OrderHistory> findBySellerId(long sellerId);
    List<OrderHistory> findByProduct_IdAndBuyer_Id(long productId, long buyerId);
    List<OrderHistory> findByProduct_IdAndProduct_Seller_Id(long productId, long sellerId);
}
