package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private UserStatus status;
    private String phone;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .status(user.getStatus())
                .phone(user.getPhone())
                .build();
    }
}
