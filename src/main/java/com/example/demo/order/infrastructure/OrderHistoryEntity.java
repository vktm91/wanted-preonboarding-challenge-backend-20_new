package com.example.demo.order.infrastructure;

import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.product.domain.Product;
import com.example.demo.product.infrastructure.ProductEntity;
import com.example.demo.user.infrastructure.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Table(name = "orderHistory")
public class OrderHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyerId")
    private UserEntity buyer;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private ProductEntity product;

    @Column(name = "price")
    private Long price;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "registDt")
    private LocalDateTime registDt;

    @Column(name = "updateDt")
    private LocalDateTime updateDt;

//    public void changeProduct(ProductEntity product) {
//        if (this.product != null) {
//            this.product.getOrderHistories().remove(this);
//        }
//
//        this.product = product;
//        product.getOrderHistories().add(this);
//    }

    public static OrderHistoryEntity from(OrderHistory orderHistory) {
        OrderHistoryEntity orderHistoryEntity = new OrderHistoryEntity();
        orderHistoryEntity.setId(orderHistory.getId());
        orderHistoryEntity.setBuyer(UserEntity.from(orderHistory.getBuyer()));
        orderHistoryEntity.setProduct(ProductEntity.from(orderHistory.getProduct()));
        orderHistoryEntity.setPrice(orderHistory.getPrice());
        orderHistoryEntity.setStatus(orderHistory.getStatus());
        orderHistoryEntity.setRegistDt(orderHistory.getRegistDt());
        orderHistoryEntity.setUpdateDt(orderHistory.getUpdateDt());
        return orderHistoryEntity;
    }

    public OrderHistory toModel() {
        return OrderHistory.builder()
                .id(id)
                .buyer(buyer.toModel())
                .product(Product.builder()
                        .id(this.product.getId())
                        .name(this.product.getName())
                        .price(this.product.getPrice())
                        .status(this.product.getStatus())
                        .count(this.product.getCount())
                        .registDt(this.product.getRegistDt())
                        .updateDt(this.product.getUpdateDt())
                        .seller(this.product.getSeller() != null ? this.product.getSeller().toModel() : null)
                        .build())
                .price(price)
                .status(status)
                .registDt(registDt)
                .updateDt(updateDt)
                .build();
    }
}
