package com.example.demo.order.infrastructure;

import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.service.port.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository {
    private final OrderHistoryJpaRepository orderHistoryJpaRepository;

    @Override
    public Optional<OrderHistory> findById(long id) {
        return orderHistoryJpaRepository.findById(id).map(OrderHistoryEntity::toModel);
    }

    @Override
    public OrderHistory save(OrderHistory orderHistory) {
        return orderHistoryJpaRepository.save(OrderHistoryEntity.from(orderHistory)).toModel();
    }

    @Override
    public List<OrderHistory> findByProduct_Id(long productId) {
        return orderHistoryJpaRepository.findByProduct_Id(productId).stream()
                .map(OrderHistoryEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderHistory> findByBuyerId(long buyerId) {
        return orderHistoryJpaRepository.findByBuyer_Id(buyerId).stream()
                .map(OrderHistoryEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderHistory> findBySellerId(long sellerId) {
        return orderHistoryJpaRepository.findByProduct_Seller_Id(sellerId).stream()
                .map(OrderHistoryEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderHistory> findByProduct_IdAndBuyer_Id(long productId, long buyerId) {
        return orderHistoryJpaRepository.findByProduct_IdAndBuyer_Id(productId, buyerId).stream()
                .map(OrderHistoryEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderHistory> findByProduct_IdAndProduct_Seller_Id(long productId, long sellerId) {
        return orderHistoryJpaRepository.findByProduct_IdAndProduct_Seller_Id(productId, sellerId).stream()
                .map(OrderHistoryEntity::toModel)
                .collect(Collectors.toList());
    }
}
