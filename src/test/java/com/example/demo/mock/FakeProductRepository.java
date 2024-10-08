package com.example.demo.mock;

import com.example.demo.product.domain.Product;
import com.example.demo.product.service.port.ProductRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeProductRepository implements ProductRepository {
    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<Product> data = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Product save(Product product) {
        if(product.getId() == null || product.getId() == 0) {
            Product newProduct = Product.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .name(product.getName())
                    .price(product.getPrice())
                    .status(product.getStatus())
                    .count(product.getCount())
                    .registDt(product.getRegistDt())
                    .seller(product.getSeller())
                    .build();
            data.add(newProduct);
            return newProduct;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), product.getId()));
            data.add(product);
            return product;
        }
    }

    @Override
    public Optional<Product> findById(long id) {
        return data.stream().filter(item -> item.getId().equals(id)).findAny();
    }
}
