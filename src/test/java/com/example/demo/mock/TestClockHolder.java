package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TestClockHolder implements ClockHolder {

    private final LocalDateTime nowDt;

    @Override
    public LocalDateTime getNowDt() {
        return nowDt;
    }
}
