package com.example.demo.order.service;

import com.example.demo.mock.TestContainer;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductStatus;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrderHistoryServiceTest {

    @Test
    void getOrderHistoriesByProductNo로_주문을_조회_할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        User seller = testContainer.userRepository.save(User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        User buyer = testContainer.userRepository.save(User.builder()
                .id(2L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        Product product = testContainer.productRepository.save(Product.builder()
                .id(1L)
                .name("얌얌쩝쩝 강아지 간식")
                .price(3000L)
                .status(ProductStatus.SALE)
                .count(3L)
                .registDt(fakeLocalDt)
                .seller(seller)
                .build());

        testContainer.orderHistoryRepository.save(OrderHistory.builder()
                .id(1L)
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(fakeLocalDt)
                .build());

        testContainer.orderHistoryRepository.save(OrderHistory.builder()
                .id(2L)
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(fakeLocalDt)
                .build());

        // when
        List<OrderHistory> result = testContainer.orderHistoryService.getOrderHistoriesByProductNo(1L);

        // then
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getBuyer().getId()).isEqualTo(2L);
        assertThat(result.get(0).getProduct().getId()).isEqualTo(1L);
        assertThat(result.get(0).getPrice()).isEqualTo(3000L);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.get(0).getRegistDt()).isEqualTo(fakeLocalDt);

        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getBuyer().getId()).isEqualTo(2L);
        assertThat(result.get(1).getProduct().getId()).isEqualTo(1L);
        assertThat(result.get(1).getPrice()).isEqualTo(3000L);
        assertThat(result.get(1).getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.get(1).getRegistDt()).isEqualTo(fakeLocalDt);
    }

    @Test
    void getOrderHistoriesByBuyerId로_주문을_조회_할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        User seller = testContainer.userRepository.save(User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        User buyer = testContainer.userRepository.save(User.builder()
                .id(2L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        Product product = testContainer.productRepository.save(Product.builder()
                .id(1L)
                .name("얌얌쩝쩝 강아지 간식")
                .price(3000L)
                .status(ProductStatus.SALE)
                .count(3L)
                .registDt(fakeLocalDt)
                .seller(seller)
                .build());

        testContainer.orderHistoryRepository.save(OrderHistory.builder()
                .id(1L)
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(fakeLocalDt)
                .build());

        testContainer.orderHistoryRepository.save(OrderHistory.builder()
                .id(2L)
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(fakeLocalDt)
                .build());

        // when
        List<OrderHistory> result = testContainer.orderHistoryService.getOrderHistoriesByBuyerId(2L);

        // then
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getBuyer().getId()).isEqualTo(2L);
        assertThat(result.get(0).getProduct().getId()).isEqualTo(1L);
        assertThat(result.get(0).getPrice()).isEqualTo(3000L);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.get(0).getRegistDt()).isEqualTo(fakeLocalDt);

        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getBuyer().getId()).isEqualTo(2L);
        assertThat(result.get(1).getProduct().getId()).isEqualTo(1L);
        assertThat(result.get(1).getPrice()).isEqualTo(3000L);
        assertThat(result.get(1).getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.get(1).getRegistDt()).isEqualTo(fakeLocalDt);
    }

    @Test
    void getOrderHistoriesBySellerId로_주문을_조회_할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        User seller = testContainer.userRepository.save(User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        User buyer = testContainer.userRepository.save(User.builder()
                .id(2L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        Product product = testContainer.productRepository.save(Product.builder()
                .id(1L)
                .name("얌얌쩝쩝 강아지 간식")
                .price(3000L)
                .status(ProductStatus.SALE)
                .count(3L)
                .registDt(fakeLocalDt)
                .seller(seller)
                .build());

        testContainer.orderHistoryRepository.save(OrderHistory.builder()
                .id(1L)
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(fakeLocalDt)
                .build());

        testContainer.orderHistoryRepository.save(OrderHistory.builder()
                .id(2L)
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(fakeLocalDt)
                .build());

        // when
        List<OrderHistory> result = testContainer.orderHistoryService.getOrderHistoriesBySellerId(1L);

        // then
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getBuyer().getId()).isEqualTo(2L);
        assertThat(result.get(0).getProduct().getId()).isEqualTo(1L);
        assertThat(result.get(0).getPrice()).isEqualTo(3000L);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.get(0).getRegistDt()).isEqualTo(fakeLocalDt);

        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getBuyer().getId()).isEqualTo(2L);
        assertThat(result.get(1).getProduct().getId()).isEqualTo(1L);
        assertThat(result.get(1).getPrice()).isEqualTo(3000L);
        assertThat(result.get(1).getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.get(1).getRegistDt()).isEqualTo(fakeLocalDt);
    }

    @Test
    void getOrderHistoriesByProductIdAndBuyerId로_주문을_조회_할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        User seller = testContainer.userRepository.save(User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        User buyer = testContainer.userRepository.save(User.builder()
                .id(2L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        Product product = testContainer.productRepository.save(Product.builder()
                .id(1L)
                .name("얌얌쩝쩝 강아지 간식")
                .price(3000L)
                .status(ProductStatus.SALE)
                .count(3L)
                .registDt(fakeLocalDt)
                .seller(seller)
                .build());

        testContainer.orderHistoryRepository.save(OrderHistory.builder()
                .id(1L)
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(fakeLocalDt)
                .build());

        testContainer.orderHistoryRepository.save(OrderHistory.builder()
                .id(2L)
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(fakeLocalDt)
                .build());

        // when
        List<OrderHistory> result = testContainer.orderHistoryService.getOrderHistoriesByProductIdAndBuyerId(1L, 2L);

        // then
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getBuyer().getId()).isEqualTo(2L);
        assertThat(result.get(0).getProduct().getId()).isEqualTo(1L);
        assertThat(result.get(0).getPrice()).isEqualTo(3000L);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.get(0).getRegistDt()).isEqualTo(fakeLocalDt);

        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getBuyer().getId()).isEqualTo(2L);
        assertThat(result.get(1).getProduct().getId()).isEqualTo(1L);
        assertThat(result.get(1).getPrice()).isEqualTo(3000L);
        assertThat(result.get(1).getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.get(1).getRegistDt()).isEqualTo(fakeLocalDt);
    }

    @Test
    void getOrderHistoriesByPorductIdAndSellerId로_주문을_조회_할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        User seller = testContainer.userRepository.save(User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        User buyer = testContainer.userRepository.save(User.builder()
                .id(2L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        Product product = testContainer.productRepository.save(Product.builder()
                .id(1L)
                .name("얌얌쩝쩝 강아지 간식")
                .price(3000L)
                .status(ProductStatus.SALE)
                .count(3L)
                .registDt(fakeLocalDt)
                .seller(seller)
                .build());

        testContainer.orderHistoryRepository.save(OrderHistory.builder()
                .id(1L)
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(fakeLocalDt)
                .build());

        testContainer.orderHistoryRepository.save(OrderHistory.builder()
                .id(2L)
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(fakeLocalDt)
                .build());

        // when
        List<OrderHistory> result = testContainer.orderHistoryService.getOrderHistoriesByPorductIdAndSellerId(1L, 1L);

        // then
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getBuyer().getId()).isEqualTo(2L);
        assertThat(result.get(0).getProduct().getId()).isEqualTo(1L);
        assertThat(result.get(0).getProduct().getSeller().getId()).isEqualTo(1L);
        assertThat(result.get(0).getPrice()).isEqualTo(3000L);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.get(0).getRegistDt()).isEqualTo(fakeLocalDt);

        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getBuyer().getId()).isEqualTo(2L);
        assertThat(result.get(1).getProduct().getId()).isEqualTo(1L);
        assertThat(result.get(0).getProduct().getSeller().getId()).isEqualTo(1L);
        assertThat(result.get(1).getPrice()).isEqualTo(3000L);
        assertThat(result.get(1).getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.get(1).getRegistDt()).isEqualTo(fakeLocalDt);
    }

}
