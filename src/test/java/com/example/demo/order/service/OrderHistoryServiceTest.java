package com.example.demo.order.service;

import com.example.demo.common.domain.exception.OrderStatusBadRequestException;
import com.example.demo.mock.TestContainer;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderHistoryUpdate;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductStatus;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class OrderHistoryServiceTest {

    @Test
    void getOrderHistoryById로_주문을_조회할_수_있다() {
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

        // when
        Optional<OrderHistory> result = testContainer.orderHistoryReadService.getOrderHistoryById(1L);

        // then
        assertThat(result).isNotEmpty();

        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getBuyer().getId()).isEqualTo(2L);
        assertThat(result.get().getProduct().getId()).isEqualTo(1L);
        assertThat(result.get().getPrice()).isEqualTo(3000L);
        assertThat(result.get().getStatus()).isEqualTo(OrderStatus.RESERVED);
    }

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
        List<OrderHistory> result = testContainer.orderHistoryReadService.getOrderHistoriesByProductNo(1L);

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
        List<OrderHistory> result = testContainer.orderHistoryReadService.getOrderHistoriesByBuyerId(2L);

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
        List<OrderHistory> result = testContainer.orderHistoryReadService.getOrderHistoriesBySellerId(1L);

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
        List<OrderHistory> result = testContainer.orderHistoryReadService.getOrderHistoriesByProductIdAndBuyerId(1L, 2L);

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
        List<OrderHistory> result = testContainer.orderHistoryReadService.getOrderHistoriesByPorductIdAndSellerId(1L, 1L);

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


    @Test
    void 주문생성후_주문상태가_RESERVED이다() {
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

        testContainer.userRepository.save(User.builder()
                .id(2L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        testContainer.productRepository.save(Product.builder()
                .id(1L)
                .name("얌얌쩝쩝 강아지 간식")
                .price(3000L)
                .status(ProductStatus.SALE)
                .count(3L)
                .registDt(fakeLocalDt)
                .seller(seller)
                .build());

        // when
        OrderHistory result = testContainer.orderHistoryService.createOrder(2L, 1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBuyer().getId()).isEqualTo(2L);
        assertThat(result.getProduct().getId()).isEqualTo(1L);
        assertThat(result.getProduct().getSeller().getId()).isEqualTo(1L);
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.getRegistDt()).isEqualTo(fakeLocalDt);
    }


    @Test
    void 주문승인후_주문상태가_APPROVED이다() {
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

        // when
        OrderHistory result = testContainer.orderHistoryService.updateStatus(OrderHistoryUpdate.builder()
                .orderHistoryId(1L)
                .statusTo(OrderStatus.APPROVED)
                .build());

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBuyer().getId()).isEqualTo(2L);
        assertThat(result.getProduct().getId()).isEqualTo(1L);
        assertThat(result.getProduct().getSeller().getId()).isEqualTo(1L);
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.APPROVED);
        assertThat(result.getRegistDt()).isEqualTo(fakeLocalDt);
    }

    @Test
    void 주문확정후_주문상태가_CONFIRMED이다() {
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
                .status(OrderStatus.APPROVED)
                .registDt(fakeLocalDt)
                .build());

        // when
        OrderHistory result = testContainer.orderHistoryService.updateStatus(OrderHistoryUpdate.builder()
                .orderHistoryId(1L)
                .statusTo(OrderStatus.CONFIRMED)
                .build());

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBuyer().getId()).isEqualTo(2L);
        assertThat(result.getProduct().getId()).isEqualTo(1L);
        assertThat(result.getProduct().getSeller().getId()).isEqualTo(1L);
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(result.getRegistDt()).isEqualTo(fakeLocalDt);
    }

    @Test
    void RESERVED_상태의_주문에_구매확정_요청이_들어오면_예외를_던진다() {
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        User seller = User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build();

        testContainer.userRepository.save(seller);

        User buyer = User.builder()
                .id(2L)
                .status(UserStatus.ACTIVE)
                .phone("01018182828")
                .build();

        testContainer.userRepository.save(buyer);

        Product product = Product.builder()
                .id(1L)
                .name("얌얌쩝쩝 강아지 간식")
                .price(3000L)
                .status(ProductStatus.SALE)
                .count(1L)
                .registDt(fakeLocalDt)
                .seller(seller)
                .build();

        testContainer.productRepository.save(product);

        testContainer.orderHistoryService.createOrder(2L, 1L);

        // when
        assertThatThrownBy(() -> {
            testContainer.orderHistoryService.updateStatus(OrderHistoryUpdate.builder()
                    .orderHistoryId(1L)
                    .statusTo(OrderStatus.CONFIRMED)
                    .build());
        }).isInstanceOf(OrderStatusBadRequestException.class);
    }
}
