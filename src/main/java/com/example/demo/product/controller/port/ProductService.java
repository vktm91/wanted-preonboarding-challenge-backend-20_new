package com.example.demo.product.controller.port;

import com.example.demo.order.domain.OrderHistory;
import com.example.demo.product.controller.response.ProductResponse;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductCreate;
import com.example.demo.product.domain.ProductUpdate;

import java.util.List;

public interface ProductService {
    Product create(ProductCreate productCreate);
    Product save(Product product);
    Product getById(long productId);
    ProductResponse getByIdAndUserId(long productId, Long userId);
    Product updateInfo(long productId, ProductUpdate productUpdate);
    Product updateStatus(long productId, List<OrderHistory> orderHistories);
    Product decreaseCount(long productId, List<OrderHistory> orderHistories);
}
