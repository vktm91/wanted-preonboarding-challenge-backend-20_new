package com.example.demo.product.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.product.controller.response.ProductResponse;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductCreate;
import com.example.demo.product.domain.ProductStatus;
import com.example.demo.product.domain.ProductUpdate;
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

public class ProductControllerTest {

    @Test
    void 사용자는_상품을_생성_할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        testContainer.initializeRequestContext("/api/products", 8080, "localhost", "http");
        URI uri = testContainer.createUri("/api/products/{id}", 1L);

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        ProductCreate productCreate = ProductCreate.builder()
                .productNm("얌얌쩝쩝 강아지 간식")
                .productPrice(3000L)
                .count(3L)
                .sellerId(1L)
                .build();

        // when
        ResponseEntity<ProductResponse> result = testContainer.productController.create(productCreate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getBody().getPrice()).isEqualTo(3000L);
        assertThat(result.getBody().getStatus()).isEqualTo(ProductStatus.SALE);
        assertThat(result.getBody().getCount()).isEqualTo(3L);
        assertThat(result.getBody().getRegistDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getBody().getUpdateDt()).isNull();
        assertThat(result.getBody().getSeller().getId()).isEqualTo(1L);
        assertThat(result.getHeaders().getLocation()).isEqualTo(uri);

        // clean up
        testContainer.clearRequestContext();
    }


    @Test
    void 상품_생성_요청값이_누락될_경우_검증오류메시지가_일치한다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> LocalDateTime.of(2024, 6, 9, 0, 0))
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        ProductCreate productCreate = ProductCreate.builder()
                .productPrice(0L)
                .build();

        // when & then
        assertThatThrownBy(() -> testContainer.validate(productCreate))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Product name must not be blank");
    }


    @Test
    void 상품_생성시_상품개수가_1보다_작을_경우_검증오류메시지가_일치한다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> LocalDateTime.of(2024, 6, 9, 0, 0))
                .build();

        testContainer.initializeRequestContext("/api/products", 8080, "localhost", "http");

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        ProductCreate productCreate = ProductCreate.builder()
                .productNm("얌얌쩝쩝 강아지 간식")
                .productPrice(3000L)
                .count(0L)
                .sellerId(1L)
                .build();

        // when & then
        assertThatThrownBy(() -> testContainer.validate(productCreate))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Count must be at least 1");
    }


    @Test
    void 사용자는_상품_상세를_조회_할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        testContainer.productRepository.save(Product.builder()
                .id(1L)
                .name("얌얌쩝쩝 강아지 간식")
                .price(3000L)
                .status(ProductStatus.SALE)
                .count(3L)
                .registDt(fakeLocalDt)
                .seller(User.builder()
                        .id(1L)
                        .status(UserStatus.ACTIVE)
                        .phone("01012341234")
                        .build())
                .build());

        // when
        ResponseEntity<ProductResponse> result = testContainer.productController.getById(1L, null);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getBody().getPrice()).isEqualTo(3000L);
        assertThat(result.getBody().getStatus()).isEqualTo(ProductStatus.SALE);
        assertThat(result.getBody().getCount()).isEqualTo(3L);
        assertThat(result.getBody().getRegistDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getBody().getUpdateDt()).isNull();
        assertThat(result.getBody().getSeller().getId()).isEqualTo(1L);
    }


    @Test
    void 사용자는_상품을_수정_할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
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
                .seller(User.builder()
                        .id(1L)
                        .status(UserStatus.ACTIVE)
                        .phone("01012341234")
                        .build())
                .build());

        // when
        ResponseEntity<ProductResponse> result = testContainer.productController.updateInfo(1L, ProductUpdate.builder()
                .productNm("얌얌쩝쩝 고양이 간식")
                .productPrice(5000L)
                .count(10L)
                .build());

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getName()).isEqualTo("얌얌쩝쩝 고양이 간식");
        assertThat(result.getBody().getPrice()).isEqualTo(5000L);
        assertThat(result.getBody().getCount()).isEqualTo(10L);
        assertThat(result.getBody().getUpdateDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getBody().getSeller().getId()).isEqualTo(1L);

        // clean up
        testContainer.clearRequestContext();
    }

    @Test
    void 상품_수정시_개수가_부적절한_경우_검증오류메시지가_일치한다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
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
                .seller(User.builder()
                        .id(1L)
                        .status(UserStatus.ACTIVE)
                        .phone("01012341234")
                        .build())
                .build());

        ProductUpdate productUpdate = ProductUpdate.builder()
                .count(-1L)
                .build();

        // when & then
        assertThatThrownBy(() -> testContainer.validate(productUpdate))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Product count must be at least 0");
    }
}
