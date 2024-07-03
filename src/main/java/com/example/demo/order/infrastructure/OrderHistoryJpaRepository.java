package com.example.demo.order.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryJpaRepository extends JpaRepository<OrderHistoryEntity, Long> {
    List<OrderHistoryEntity> findByProduct_Id(long productId);
    List<OrderHistoryEntity> findByBuyer_Id(long buyerId);
    List<OrderHistoryEntity> findByProduct_Seller_Id(long sellerId);
    List<OrderHistoryEntity> findByProduct_IdAndBuyer_Id(long productId, long buyerId);
    List<OrderHistoryEntity> findByProduct_IdAndProduct_Seller_Id(long productId, long buyerId);
}
