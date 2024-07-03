package com.example.demo.product.controller.port;

import com.example.demo.product.controller.response.ProductResponse;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductCreate;
import com.example.demo.product.domain.ProductUpdate;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product create(ProductCreate productCreate);
    Product getById(long productId);
    ProductResponse getByIdAndUserId(long productId, Long userId);
    Product update(long productId, ProductUpdate productUpdate);

//    Product updateStatus(long productId);
//    Product decreaseCount(long count);
//    List<Product> getForBuyer(long userId);
//    List<Product> getForSeller(long sellerId);
}
