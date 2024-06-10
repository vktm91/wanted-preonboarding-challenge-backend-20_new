package com.example.demo.product.infrastructure;

import com.example.demo.product.domain.Product;
import com.example.demo.product.service.port.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository{
    private final ProductJpaRepository productJpaRepository;

//    @Override
//    public Product order(Product product) {
//        return null;
//    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(ProductEntity.from(product)).toModel();
    }

    @Override
    public Optional<Product> findById(long id) {
        return productJpaRepository.findById(id).map(ProductEntity::toModel);
    }
}
