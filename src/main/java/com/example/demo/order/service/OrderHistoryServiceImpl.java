package com.example.demo.order.service;

import com.example.demo.order.controller.port.OrderHistoryService;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.service.port.OrderHistoryRepository;
import com.example.demo.product.service.port.ProductRepository;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class OrderHistoryServiceImpl implements OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

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
}
