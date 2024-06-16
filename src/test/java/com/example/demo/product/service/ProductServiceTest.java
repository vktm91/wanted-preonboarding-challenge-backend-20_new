package com.example.demo.product.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductCreate;
import com.example.demo.product.domain.ProductStatus;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ProductServiceTest {

    @Test
    void 회원은_상품을_등록_할_수_있다() {
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

        ProductCreate productCreate = ProductCreate.builder()
                .productNm("얌얌쩝쩝 강아지 간식")
                .productPrice(3000L)
                .count(3L)
                .sellerId(1L)
                .build();

        // when
        Product result = testContainer.productService.create(productCreate);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.SALE);
        assertThat(result.getCount()).isEqualTo(3L);
        assertThat(result.getRegistDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getUpdateDt()).isNull();
        assertThat(result.getSeller().getId()).isEqualTo(1L);
    }

    @Test
    void 존재하지_않는_회원ID_를_이용하여_상품을_등록할_시_에러를_던진다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> LocalDateTime.of(2024, 6, 9, 0, 0))
                .build();

        ProductCreate productCreate = ProductCreate.builder()
                .productNm("얌얌쩝쩝 강아지 간식")
                .productPrice(3000L)
                .count(3L)
                .sellerId(1L)
                .build();

        // when
        assertThatThrownBy(() -> {
            testContainer.productService.create(productCreate);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 상품_상세를_불러올_수_있다() {
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

        ProductCreate productCreate = ProductCreate.builder()
                .productNm("얌얌쩝쩝 강아지 간식")
                .productPrice(3000L)
                .count(3L)
                .sellerId(1L)
                .build();

        testContainer.productService.create(productCreate);

        // when
        Product result = testContainer.productService.getById(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.SALE);
        assertThat(result.getCount()).isEqualTo(3L);
        assertThat(result.getRegistDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getUpdateDt()).isNull();
        assertThat(result.getSeller().getId()).isEqualTo(1L);
    }

    @Test
    void 추가_판매가_가능한_수량이_남아있는_경우_상품_상태가_판매중_이다() {

    }

    @Test
    void 추가_판매가_불가능하고_현재_구매확정을_대기하고_있는_경우_상품_상태가_예약중_이다() {

    }

    @Test
    void 모든_수량에_대해_모든_구매자가_모두_구매확정한_경우_상품_상태가_완료_이다() {

    }
}
