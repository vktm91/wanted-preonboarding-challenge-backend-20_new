package com.example.demo.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCreate {

    private final String phone;

    @Builder
    public UserCreate(
        @JsonProperty("phone") String phone) {
        this.phone = phone;
    }
}
