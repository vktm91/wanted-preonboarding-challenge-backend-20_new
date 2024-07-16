package com.example.demo.common.domain.exception;

public class ProductCalculatorException extends RuntimeException {

    public ProductCalculatorException() {
        super("ProductCalculator cannot operate without orderHistory for the product.");
    }

}
