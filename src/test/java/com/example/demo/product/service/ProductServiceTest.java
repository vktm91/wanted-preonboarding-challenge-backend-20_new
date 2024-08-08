package com.example.demo.product.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.order.domain.OrderHistoryUpdate;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.product.controller.response.ProductResponse;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductCreate;
import com.example.demo.product.domain.ProductStatus;
import com.example.demo.product.domain.ProductUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ProductServiceTest {

    @Test
    void create을_이용하여_상품을_생성_할_수_있다() {
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
            testContainer.productController.create(productCreate);
        }).isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void 사용자가_존재하지_않는_상품을_조회하는_경우_에러가_난다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        // when & then
        assertThatThrownBy(() -> {
            testContainer.productController.getById(1L, null);
        }).isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void getByIdAndUserId를_이용하여_비회원은_주문정보를_제외한_상품_상세를_불러올_수_있다() {
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
        ProductResponse result = testContainer.productService.getByIdAndUserId(1L, null);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.SALE);
        assertThat(result.getCount()).isEqualTo(3L);
        assertThat(result.getRegistDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getUpdateDt()).isNull();
        assertThat(result.getSeller().getId()).isEqualTo(1L);
        assertThat(result.getSellerOrderHistories().isEmpty()).isTrue();
        assertThat(result.getBuyerOrderHistories().isEmpty()).isTrue();
    }


    @Test
    void getByIdAndUserId를_이용하여_seller는_주문_내역과_함께_상품_상세를_불러올_수_있다() {
        // given
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
                .count(3L)
                .registDt(fakeLocalDt)
                .seller(seller)
                .build();

        testContainer.productRepository.save(product);

        testContainer.orderHistoryRepository.save(OrderHistory.builder()
                .id(1L)
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(fakeLocalDt)
                .build());

        // when
        ProductResponse result = testContainer.productService.getByIdAndUserId(1L, 1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.SALE);
        assertThat(result.getCount()).isEqualTo(3L);
        assertThat(result.getRegistDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getUpdateDt()).isNull();
        assertThat(result.getSeller().getId()).isEqualTo(1L);

        assertThat(!result.getSellerOrderHistories().isEmpty()).isTrue();
        assertThat(result.getSellerOrderHistories().get(0).getId()).isEqualTo(1L);
        assertThat(result.getSellerOrderHistories().get(0).getBuyer().getId()).isEqualTo(2L);
        assertThat(result.getSellerOrderHistories().get(0).getProduct().getId()).isEqualTo(1L);
        assertThat(result.getSellerOrderHistories().get(0).getPrice()).isEqualTo(3000L);
        assertThat(result.getSellerOrderHistories().get(0).getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.getSellerOrderHistories().get(0).getRegistDt()).isEqualTo(fakeLocalDt);

        assertThat(result.getBuyerOrderHistories().isEmpty()).isTrue();
    }


    @Test
    void getByIdAndUserId를_이용하여_buyer는_주문_내역과_함께_상품_상세를_불러올_수_있다() {
        // given
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
                .count(3L)
                .registDt(fakeLocalDt)
                .seller(seller)
                .build();

        testContainer.productRepository.save(product);

        testContainer.orderHistoryRepository.save(OrderHistory.builder()
                .id(1L)
                .buyer(buyer)
                .product(product)
                .price(product.getPrice())
                .status(OrderStatus.RESERVED)
                .registDt(fakeLocalDt)
                .build());

        // when
        ProductResponse result = testContainer.productService.getByIdAndUserId(1L, 2L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.SALE);
        assertThat(result.getCount()).isEqualTo(3L);
        assertThat(result.getRegistDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getUpdateDt()).isNull();
        assertThat(result.getSeller().getId()).isEqualTo(1L);

        assertThat(result.getSellerOrderHistories().isEmpty()).isTrue();

        assertThat(!result.getBuyerOrderHistories().isEmpty()).isTrue();
        assertThat(result.getBuyerOrderHistories().get(0).getId()).isEqualTo(1L);
        assertThat(result.getBuyerOrderHistories().get(0).getBuyer().getId()).isEqualTo(2L);
        assertThat(result.getBuyerOrderHistories().get(0).getProduct().getId()).isEqualTo(1L);
        assertThat(result.getBuyerOrderHistories().get(0).getPrice()).isEqualTo(3000L);
        assertThat(result.getBuyerOrderHistories().get(0).getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.getBuyerOrderHistories().get(0).getRegistDt()).isEqualTo(fakeLocalDt);
    }


    @Test
    void 추가_판매가_가능한_수량이_남아있는_경우_상품_상태가_판매중_이다() {
        // given
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
                .count(3L)
                .registDt(fakeLocalDt)
                .seller(seller)
                .build();

        testContainer.productRepository.save(product);

        testContainer.orderHistoryService.createOrder(2L, 1L);

        // when
        Product result = testContainer.productService.getById(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.SALE);
        assertThat(result.getCount()).isEqualTo(2L);
        assertThat(result.getUpdateDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getSeller().getId()).isEqualTo(1L);
    }


    @Test
    void 추가_판매가_불가능하고_현재_구매확정을_대기하고_있는_경우_상품_상태가_예약중_이다() {
        // given
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
        Product result = testContainer.productService.getById(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.RESERVED);
        assertThat(result.getCount()).isEqualTo(0L);
        assertThat(result.getUpdateDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getSeller().getId()).isEqualTo(1L);
    }


    @Test
    void 모든_수량에_대해_모든_구매자가_모두_구매확정한_경우_상품_상태가_완료_이다() {
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

        //// 상품 생성
        testContainer.productRepository.save(product);

        //// 주문 생성
        System.out.println("=======================================   주문 생성 시작    =======================================");
        OrderHistory order = testContainer.orderHistoryService.createOrder(2L, 1L);

        System.out.println("!!!!! order: " + order.getProduct().getOrderHistories());

        testContainer.productService.getById(1L);

        ///// 주문 승인 (updateStatus1)
        System.out.println("=======================================   주문 승인 시작    =======================================");

        testContainer.orderHistoryService.updateStatus(OrderHistoryUpdate.builder()
                .orderHistoryId(1L)
                .statusTo(OrderStatus.APPROVED)
                .build());   // 이거하고 product의 orderHistory 두개가 됨

        ///// 주문 확정 (updateStatus2)
        System.out.println("=======================================   주문 확정 시작    =======================================");

        testContainer.orderHistoryService.updateStatus(OrderHistoryUpdate.builder()
                .orderHistoryId(1L)
                .statusTo(OrderStatus.CONFIRMED)
                .build());

        // when
        Product result = testContainer.productService.getById(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("얌얌쩝쩝 강아지 간식");
        assertThat(result.getPrice()).isEqualTo(3000L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.COMPLETED);
        assertThat(result.getCount()).isEqualTo(0L);
        assertThat(result.getUpdateDt()).isEqualTo(fakeLocalDt);
        assertThat(result.getSeller().getId()).isEqualTo(1L);
    }


    @Test
    void 존재하지_않는_사용자ID로_상품을_수정하려는_경우_에러가_난다() {
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

        // when & then
        assertThatThrownBy(() -> {
            testContainer.productController.updateInfo(2L, ProductUpdate.builder()
                    .productNm("얌얌쩝쩝 고양이 간식")
                    .productPrice(5000L)
                    .count(10L)
                    .build());
        }).isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void 존재하지_않는_상품을_수정하는_경우_에러가_난다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        // when & then
        assertThatThrownBy(() -> {
            testContainer.productController.updateInfo(1L, ProductUpdate.builder()
                    .productNm("얌얌쩝쩝 강아지 간식")
                    .productPrice(1000L)
                    .count(3L)
                    .build());
        }).isInstanceOf(ResourceNotFoundException.class);
    }
}
