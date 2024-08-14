package com.example.demo.order.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductStatus;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrderHistoryTest {

    @Test
    public void 주문을_생성할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);

        User seller = User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build();

        User buyer = User.builder()
                .id(2L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("얌얌쩝쩝 강아지 간식")
                .price(3000L)
                .status(ProductStatus.SALE)
                .count(3L)
                .registDt(fakeLocalDt)
                .seller(seller)
                .build();

        // when
        OrderHistory result = OrderHistory.from(buyer, product, new TestClockHolder(fakeLocalDt));

        // then
        assertThat(result.getBuyer().getId()).isEqualTo(2L);
        assertThat(result.getProduct().getId()).isEqualTo(1L);
        assertThat(result.getProduct().getSeller().getId()).isEqualTo(1L);
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.getRegistDt()).isEqualTo(fakeLocalDt);
    }

    @Test
    public void 주문을_상태를_변경할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);

        User seller = User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build();

        User buyer = User.builder()
                .id(2L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("얌얌쩝쩝 강아지 간식")
                .price(3000L)
                .status(ProductStatus.SALE)
                .count(3L)
                .registDt(fakeLocalDt)
                .seller(seller)
                .build();

        OrderHistory orderHistory = OrderHistory.from(buyer, product, new TestClockHolder(fakeLocalDt));

        // when
        OrderHistory result = orderHistory.updateStatus(OrderStatus.APPROVED, new TestClockHolder(fakeLocalDt));

        // then
        assertThat(result.getBuyer().getId()).isEqualTo(2L);
        assertThat(result.getProduct().getId()).isEqualTo(1L);
        assertThat(result.getProduct().getSeller().getId()).isEqualTo(1L);
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.APPROVED);
        assertThat(result.getUpdateDt()).isEqualTo(fakeLocalDt);
    }
}
