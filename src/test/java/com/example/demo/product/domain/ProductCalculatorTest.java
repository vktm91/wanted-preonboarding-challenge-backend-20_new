package com.example.demo.product.domain;

import com.example.demo.mock.TestContainer;
import com.example.demo.order.domain.OrderHistory;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductCalculatorTest {
    // 추가 판매가 가능한 수량이 남아있는 경우 - 판매중
    // 추가 판매가 불가능하고 현재 구매확정을 대기하고 있는 경우 - 예약중
    // 모든 수량에 대해 모든 구매자가 모두 구매확정한 경우 - 완료



    @Test
    void 상품의_개수가_0보다_작을_경우_에러를_던진다() {

    }

    @Test
    void 주문이_없는_상품의_상태는_판매중이다() {
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

        // when
        ProductStatus result = ProductCalculator.calculateNewStatus(1L, null);

        // then
        assertThat(result).isEqualTo(ProductStatus.SALE);
    }

    @Test
    void 상품의_개수가_0보다_클_경우_상품의_상태는_판매중으로_계산된다() {
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
                .registDt(fakeLocalDt)
                .build());

        List<OrderHistory> orderHistories = testContainer.orderHistoryRepository.findByProduct_Id(2L);

        // when
        ProductStatus result = ProductCalculator.calculateNewStatus(1L, orderHistories);

        // then
        assertThat(result).isEqualTo(ProductStatus.SALE);
    }

    @Test
    void 상품의_개수가_0이면서_구매확정_대기중인_주문이_있는_경우_예약중으로_계산된다() {

    }

    @Test
    void 상품의_개수가_0이면서_모든_주문이_구매확정인_경우_완료로_계산된다() {

    }
}
