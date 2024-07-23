package com.example.demo.product.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.order.controller.port.OrderHistoryReadService;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.product.controller.port.ProductService;
import com.example.demo.product.controller.response.ProductResponse;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductCreate;
import com.example.demo.product.domain.ProductStatus;
import com.example.demo.product.domain.ProductUpdate;
import com.example.demo.product.service.port.ProductRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.port.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderHistoryReadService orderHistoryReadService;
    private final ClockHolder clockHolder;

    @Override
    @Transactional
    public Product create(ProductCreate productCreate) {
        User user = userRepository.getById(productCreate.getSellerId());
        Product newProduct = Product.from(user, productCreate, clockHolder);

        return productRepository.save(newProduct);
    }

    @Override
    @Transactional
    public Product save(Product product) {
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
            orderHistoriesByBuyerId.addAll(orderHistoryReadService.getOrderHistoriesByBuyerId(userId));
            orderHistoriesBySellerId.addAll(orderHistoryReadService.getOrderHistoriesBySellerId(userId));
        }

        return ProductResponse.from(product, orderHistoriesBySellerId, orderHistoriesByBuyerId);
    }

    @Override
    @Transactional
    public Product updateInfo(long productId, ProductUpdate productUpdate) {
        Product product = getById(productId);

        List<OrderHistory> orderHistories = product.getOrderHistories();

        product = product.updateInfo(productUpdate, clockHolder, orderHistories);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product decreaseCount(long productId) {
        Product product = getById(productId);

        List<OrderHistory> orderHistories = product.getOrderHistories();

        product = product.decreaseCount(clockHolder, orderHistories);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateStatus(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        ProductStatus asisStatus = product.getStatus();

        List<OrderHistory> orderHistories = product.getOrderHistories();
        product = product.updateStatus(clockHolder, orderHistories);

        if (!asisStatus.equals(product.getStatus())) {
            return productRepository.save(product);
        } else {
            return product;
        }
    }

}
