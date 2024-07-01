package com.example.demo.order.infrastructure;

import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.product.infrastructure.ProductEntity;
import com.example.demo.user.infrastructure.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "orderHistories")
public class OrderHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private UserEntity buyer;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "price")
    private Long price;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public void changeProduct(ProductEntity product) {
        if (this.product != null) {
            this.product.getOrderHistories().remove(this);
        }

        this.product = product;
        product.getOrderHistories().add(this);
    }


    public static OrderHistoryEntity from(OrderHistory orderHistory) {
        OrderHistoryEntity orderHistoryEntity = new OrderHistoryEntity();
        orderHistoryEntity.setId(orderHistory.getId());
        orderHistoryEntity.setBuyer(UserEntity.from(orderHistory.getBuyer()));
        orderHistoryEntity.setProduct(ProductEntity.from(orderHistory.getProduct()));
        orderHistoryEntity.setPrice(orderHistory.getPrice());
        orderHistoryEntity.setStatus(orderHistory.getStatus());
        return orderHistoryEntity;
    }

    public OrderHistory toModel() {
        return OrderHistory.builder()
                .id(id)
                .buyer(buyer.toModel())
                .product(product.toModel())
                .price(price)
                .status(status)
                .build();
    }
}
