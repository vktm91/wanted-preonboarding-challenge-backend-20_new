package com.example.demo.common.infrastructure;

import com.example.demo.common.service.port.ClockHolder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class SystemClockHolder implements ClockHolder {

    @Override
    public LocalDateTime getNowDt() {
        return LocalDateTime.now();
    }
}
