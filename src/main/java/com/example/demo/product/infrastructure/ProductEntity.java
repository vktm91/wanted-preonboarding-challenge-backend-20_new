package com.example.demo.product.infrastructure;

import com.example.demo.order.infrastructure.OrderHistoryEntity;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductStatus;
import com.example.demo.user.infrastructure.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private Long price;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column
    private Long count;

    @Column
    private LocalDateTime registDt;


    @Column
    private LocalDateTime updateDt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "sellerId")
    private UserEntity seller;

    @ToString.Exclude
    @OneToMany(mappedBy = "product")
    private List<OrderHistoryEntity> orderHistories = new ArrayList<>();

    public static ProductEntity from(Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.id = product.getId();
        productEntity.name = product.getName();
        productEntity.price = product.getPrice();
        productEntity.status = product.getStatus();
        productEntity.count = product.getCount();
        productEntity.registDt = product.getRegistDt();
        productEntity.updateDt = product.getUpdateDt();
        productEntity.seller = UserEntity.from(product.getSeller());
//        productEntity.orderHistories = product.getOrderHistories().stream()
//                .map(OrderHistoryEntity::from)
//                .collect(Collectors.toList());
        return productEntity;
    }

    public Product toModel() {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .status(status)
                .count(count)
                .registDt(registDt)
                .updateDt(updateDt)
                .seller(seller.toModel())
                .orderHistories(orderHistories.stream()
                        .map(OrderHistoryEntity::toModel)
                        .collect(Collectors.toList()))
                .build();
    }
}
