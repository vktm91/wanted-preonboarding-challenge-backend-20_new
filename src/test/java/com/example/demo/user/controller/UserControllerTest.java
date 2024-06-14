package com.example.demo.user.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserControllerTest {

    @Test
    void 회원을_생성_할_수_있다() {
        // given
        LocalDateTime fakeLocalDt = LocalDateTime.of(2024, 6, 9, 0, 0);
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> fakeLocalDt)
                .build();

        testContainer.initializeRequestContext("/api/users", 8080, "localhost", "http");
        URI uri = testContainer.createUri("/api/users/{id}", 1L);

        UserCreate userCreate = UserCreate.builder()
                .phone("01012341234")
                .build();

        // when
        ResponseEntity<UserResponse> result = testContainer.userCreateController.create(userCreate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getPhone()).isEqualTo("01012341234");
        assertThat(result.getHeaders().getLocation()).isEqualTo(uri);

        // clean up
        testContainer.clearRequestContext();
    }
}
