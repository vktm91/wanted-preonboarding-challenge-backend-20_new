package com.example.demo.product.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.order.controller.port.OrderHistoryService;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.product.controller.port.ProductService;
import com.example.demo.product.controller.response.ProductResponse;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductCreate;
import com.example.demo.product.domain.ProductUpdate;
import com.example.demo.product.service.port.ProductRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderHistoryService orderHistoryService;
    private final ClockHolder clockHolder;

    @Override
    public Product create(ProductCreate productCreate) {
        User user = userRepository.getById(productCreate.getSellerId());
        Product product = Product.from(user, productCreate, clockHolder);

        return productRepository.save(product);
    }

    @Override
    public Product getById(long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));
    }

    @Override
    public ProductResponse getByIdAndUserId(long productId, Long userId) {
        Product product = getById(productId);

        List<OrderHistory> orderHistoriesByBuyerId = new ArrayList<>();
        List<OrderHistory> orderHistoriesBySellerId = new ArrayList<>();
        if (userId != null) {
            orderHistoriesByBuyerId.addAll(orderHistoryService.getOrderHistoriesByBuyerId(userId));
            orderHistoriesBySellerId.addAll(orderHistoryService.getOrderHistoriesBySellerId(userId));
        }

        return ProductResponse.fromWithOrderHistories(product, orderHistoriesByBuyerId, orderHistoriesBySellerId);
    }

    @Override
    public Product update(long productId, ProductUpdate productUpdate) {
        Product product = getById(productId);

        List<OrderHistory> orderHistories = orderHistoryService.getOrderHistoriesByProductNo(productId);
        product = product.update(productUpdate, orderHistories, clockHolder);

        return productRepository.save(product);
    }
//
//    @Override
//    public Product updateStatus(long productId) {
//        Product product = getById(productId);
//        product.updateStatus();
//        return
//    }
//
//    @Override
//    public Product decreaseCount(long count) {
//        return null;
//    }
//
//    @Override
//    public List<Product> getForBuyer(long userId) {
//        return null;
//    }
//
//    @Override
//    public List<Product> getForSeller(long sellerId) {
//        return null;
//    }
}
