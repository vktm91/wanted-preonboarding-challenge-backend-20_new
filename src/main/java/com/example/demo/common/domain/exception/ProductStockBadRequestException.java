package com.example.demo.common.domain.exception;

public class ProductStockBadRequestException extends RuntimeException {
    public ProductStockBadRequestException() {
        super("재고가 남아있지 않습니다.");
    }

    public ProductStockBadRequestException(Long stockCount) {
        super("재고가 남아있지 않습니다. 남은 수량: " + stockCount);
    }
}
