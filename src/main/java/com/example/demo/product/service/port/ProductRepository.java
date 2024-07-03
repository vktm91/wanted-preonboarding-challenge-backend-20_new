package com.example.demo.product.service.port;

import com.example.demo.product.domain.Product;

import java.util.Optional;

public interface ProductRepository {
//    Product order(Product product);
    Product save(Product product);
    Optional<Product> findById(long id);
}
