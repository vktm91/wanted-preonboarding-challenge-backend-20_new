package com.example.demo.product.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductTest {

    @Test
    public void 상품을_만들_수_있다() {
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);

        // given
        ProductCreate productCreate = ProductCreate.builder()
                .productNm("얌얌쩝쩝 강아지 간식")
                .productPrice(3000L)
                .count(3L)
                .sellerId(1L)
                .build();

        User seller = User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build();

        // when
        Product result = Product.from(seller, productCreate, new TestClockHolder(fakeLocalDt));

        // then
        assertThat(result.getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.SALE);
        assertThat(result.getCount()).isEqualTo(3L);
        assertThat(result.getRegistDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getUpdateDt()).isNull();
        assertThat(result.getSeller().getId()).isEqualTo(1L);
    }

    @Test
    public void 상품을_수정할_수_있다() {
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);

        // given
        ProductCreate productCreate = ProductCreate.builder()
                .productNm("얌얌쩝쩝 강아지 간식")
                .productPrice(3000L)
                .count(3L)
                .sellerId(1L)
                .build();

        User seller = User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build();

        Product product = Product.from(seller, productCreate, new TestClockHolder(fakeLocalDt));

        ProductUpdate productUpdate = ProductUpdate.builder()
                .productNm("얌얌쩝쩝 고양이 간식")
                .productPrice(7000L)
                .count(1L)
                .build();

        // when
        Product result = product.updateInfo(productUpdate, new TestClockHolder(fakeLocalDt), null);

        // then
        assertThat(result.getName()).isEqualTo("얌얌쩝쩝 고양이 간식");
        assertThat(result.getPrice()).isEqualTo(7000L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.SALE);
        assertThat(result.getCount()).isEqualTo(1L);
        assertThat(result.getUpdateDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getSeller().getId()).isEqualTo(1L);
    }

    @Test
    public void 상품_상태를_변경할_수_있다() {
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);

        // given
        ProductCreate productCreate = ProductCreate.builder()
                .productNm("얌얌쩝쩝 강아지 간식")
                .productPrice(3000L)
                .count(0L)
                .sellerId(1L)
                .build();

        User seller = User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build();

        Product product = Product.from(seller, productCreate, new TestClockHolder(fakeLocalDt));

        OrderHistory orderHistory = OrderHistory.builder()
                .id(1L)
                .product(product)
                .status(OrderStatus.APPROVED)
                .build();
        List<OrderHistory> orderHistories = List.of(orderHistory);

        // when
        Product result = product.updateStatus(new TestClockHolder(fakeLocalDt), orderHistories);

        // then
        assertThat(result.getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.RESERVED);
        assertThat(result.getCount()).isEqualTo(0L);
        assertThat(result.getUpdateDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getSeller().getId()).isEqualTo(1L);
    }

    @Test
    public void 상품_재고를_감소시킬_수_있다() {
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);

        // given
        ProductCreate productCreate = ProductCreate.builder()
                .productNm("얌얌쩝쩝 강아지 간식")
                .productPrice(3000L)
                .count(1L)
                .sellerId(1L)
                .build();

        User seller = User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build();

        Product product = Product.from(seller, productCreate, new TestClockHolder(fakeLocalDt));

        OrderHistory orderHistory = OrderHistory.builder()
                .id(1L)
                .product(product)
                .status(OrderStatus.APPROVED)
                .build();
        List<OrderHistory> orderHistories = List.of(orderHistory);

        // when
        Product result = product.decreaseCount(new TestClockHolder(fakeLocalDt), orderHistories);

        // then
        assertThat(result.getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.RESERVED);
        assertThat(result.getCount()).isEqualTo(0L);
        assertThat(result.getUpdateDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getSeller().getId()).isEqualTo(1L);
    }
}
