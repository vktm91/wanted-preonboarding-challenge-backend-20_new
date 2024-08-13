package com.example.demo.order.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.order.controller.response.OrderHistoriesResponse;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderHistoryCreate;
import com.example.demo.order.domain.OrderHistoryUpdate;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductStatus;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class OrderHistoryControllerTest {
    @Test
    void 회원은_OrderRequestDto_를_이용하여_상품을_구매할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        testContainer.initializeRequestContext("/api/orders", 8080, "localhost", "http");
        URI uri = testContainer.createUri("/api/orders/{id}", 1L);

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

        OrderHistoryCreate orderHistoryCreate = OrderHistoryCreate.builder()
                .buyerId(2L)
                .productId(1L)
                .build();

        // when
        ResponseEntity<OrderHistoriesResponse> result = testContainer.orderHistoryController.create(orderHistoryCreate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getBuyerId()).isEqualTo(2L);
        assertThat(result.getBody().getProductId()).isEqualTo(1L);
        assertThat(result.getBody().getPrice()).isEqualTo(3000L);
        assertThat(result.getBody().getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.getBody().getRegistDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getHeaders().getLocation()).isEqualTo(uri);

        // clean up
        testContainer.clearRequestContext();
    }

    @Test
    void 주문_생성_요청값이_누락될_경우_검증오류메시지가_일치한다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        OrderHistoryCreate orderHistoryCreate = OrderHistoryCreate.builder()
                .buyerId(2L)
                .build();

        // when & then
        assertThatThrownBy(() -> testContainer.validate(orderHistoryCreate))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("ProductId must not be null");
    }

    @Test
    void OrderHistoryUpdate를_이용하여_주문_상태를_업데이트할_수_있다() {
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
        ResponseEntity<OrderHistoriesResponse> result = testContainer.orderHistoryController.update(
                OrderHistoryUpdate.builder()
                        .orderHistoryId(1L)
                        .statusTo(OrderStatus.APPROVED)
                        .build()
        );

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getBuyerId()).isEqualTo(2L);
        assertThat(result.getBody().getProductId()).isEqualTo(1L);
        assertThat(result.getBody().getPrice()).isEqualTo(3000L);
        assertThat(result.getBody().getStatus()).isEqualTo(OrderStatus.APPROVED);
        assertThat(result.getBody().getRegistDt()).isEqualTo(fakeLocalDt);
    }

    @Test
    void OrderHistoryUpdate_요청값이_누락될_경우_검증오류메시지가_일치한다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        OrderHistoryUpdate orderHistoryUpdate = OrderHistoryUpdate.builder().orderHistoryId(1L).build();

        // when & then
        assertThatThrownBy(() -> testContainer.validate(orderHistoryUpdate))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("statusTo must not be null");
    }

}
