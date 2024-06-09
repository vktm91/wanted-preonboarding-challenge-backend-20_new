package com.example.demo.mock;

import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.service.port.OrderHistoryRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeOrderHistoryRepository implements OrderHistoryRepository {
    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<OrderHistory> data = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Optional<OrderHistory> findById(long id) {
        return data.stream().filter(item -> item.getId().equals(id)).findAny();
    }

    @Override
    public OrderHistory save(OrderHistory user) {
        return null;
    }

    @Override
    public List<OrderHistory> findByProduct_Id(long productId) {
        return null;
    }

    @Override
    public List<OrderHistory> findByBuyerId(long buyerId) {
        return null;
    }

    @Override
    public List<OrderHistory> findBySellerId(long sellerId) {
        return null;
    }

    @Override
    public List<OrderHistory> findByProduct_IdAndBuyer_Id(long productId, long buyerId) {
        return null;
    }

    @Override
    public List<OrderHistory> findByProduct_IdAndProduct_Seller_Id(long productId, long sellerId) {
        return null;
    }
}
