package com.example.demo.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@ToString
@Slf4j
public class User {
    private final Long id;
    private final UserStatus status;
    private final String phone;
    private final LocalDateTime lastLoginDt;

    @Builder
    public User(Long id, UserStatus status, String phone, LocalDateTime lastLoginDt) {
        this.id = id;
        this.status = status;
        this.phone = phone;
        this.lastLoginDt = lastLoginDt;
    }

    public static User from(UserCreate userCreate) {
        return User.builder()
                .status(UserStatus.ACTIVE)
                .phone(userCreate.getPhone())
                .build();
    }

//    public User login(ClockHolder clockHolder) {
//        return User.builder()
//                .id(id)
//                .status(status)
//                .phone(phone)
//                .lastLoginDt(clockHolder.getNowDt())
//                .build();
//    }
}
