package com.example.demo.order.domain;

public enum OrderStatus {
    RESERVED(0),
    APPROVED(1),
    CONFIRMED(2);

    private final int step;

    OrderStatus(int step) {
        this.step = step;
    }

    public int getStep() {
        return step;
    }
}
