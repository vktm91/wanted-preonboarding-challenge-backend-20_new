package com.example.demo.user.service;

import com.example.demo.mock.TestContainer;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserServiceTest {


    @Test
    void getById는_USER_STATUS가_ACTIVE인_회원을_조회_할_수_있다() {
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

        // when
        User result = testContainer.userService.getById(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getPhone()).isEqualTo("01012341234");
    }

    @Test
    void create로_회원을_등록할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        UserCreate userCreate = UserCreate.builder()
                .phone("01012341234")
                .build();

        // when
        User result = testContainer.userService.create(userCreate);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getPhone()).isEqualTo("01012341234");
    }
}
