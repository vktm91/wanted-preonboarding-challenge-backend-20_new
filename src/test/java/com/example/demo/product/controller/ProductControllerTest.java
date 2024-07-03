package com.example.demo.product.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.product.controller.response.ProductResponse;
import com.example.demo.product.domain.ProductCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ProductControllerTest {

    // 유효성 검사
    // 도메인 모델 / 애플리케이션 레이어(dto) / 인프라스트럭처 레이어(db 필드 제약조건)

    @Test
    void 사용자는_상품을_생성할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> LocalDateTime.of(2024, 6, 9, 0, 0))
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
        assertThat(result.getBody().getCount()).isEqualTo(3L);
        assertThat(result.getBody().getSeller().getId()).isEqualTo(1L);
        assertThat(result.getHeaders().getLocation()).isEqualTo(uri);

        // clean up
        testContainer.clearRequestContext();
    }


    @Test
    void 상품_생성_요청이_부적절할_경우_에러가_난다() {
        // 상품 생성 요청 시 필수 필드가 누락된 경우 등을 시뮬레이션
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> LocalDateTime.of(2024, 6, 9, 0, 0))
                .build();

        testContainer.initializeRequestContext("/api/products", 8080, "localhost", "http");
        URI uri = testContainer.createUri("/api/products/{id}", 1L);

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .phone("01012341234")
                .build());

        ProductCreate productCreate = ProductCreate.builder()
                .build();

        // when
        assertThatThrownBy(() -> {
            testContainer.productController.create(productCreate);
        }).isInstanceOf(ResourceNotFoundException.class);




        ResponseEntity<ProductResponse> result = testContainer.productController.create(productCreate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getBody().getPrice()).isEqualTo(3000L);
        assertThat(result.getBody().getCount()).isEqualTo(3L);
        assertThat(result.getBody().getSeller().getId()).isEqualTo(1L);
        assertThat(result.getHeaders().getLocation()).isEqualTo(uri);

        // clean up
        testContainer.clearRequestContext();
    }

    // getById

    @Test
    void 사용자는_상품을_단건_조회_할_수_있다() {

        // given

        // when

        // then

    }

    @Test
    void 사용자가_존재하지_않는_상품을_조회하는_경우_에러가_난다() {

        // given

        // when

        // then

    }

    @Test
    void 잘못된_ID_형식으로_상품을_조회하는_경우_에러가_난다() {

        // given

        // when

        // then
        // 잘못된 ID 형식으로 상품 조회 시나리오 시뮬레이션
    }

    // update

    @Test
    void 사용자는_상품을_수정_할_수_있다() {

        // given

        // when

        // then

    }

    @Test
    void 수정_권한이_없는_상품을_수정하려는_경우_에러가_난다() {

        // given

        // when

        // then
        // 사용자가 수정 권한이 없는 상품 수정 시나리오 시뮬레이션
    }

    @Test
    void 상품_수정_요청이_부적절할_경우_에러가_난다() {

        // given

        // when

        // then
        // 상품 수정 요청 시 부적절한 바디를 전송한 경우 등을 시뮬레이션
    }

}
