package com.example.demo.common.domain.exception;

public class NegativeProductCountException extends RuntimeException {

    public NegativeProductCountException() {
        super("Product count cannot be negative.");
    }

    public NegativeProductCountException(long count) {
        super("Product count cannot be negative: " + count + " is invalid.");
    }
}
