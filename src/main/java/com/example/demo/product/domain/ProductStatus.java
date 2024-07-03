package com.example.demo.product.domain;

public enum ProductStatus {
    SALE("판매중"),
    RESERVED("예약중"),
    COMPLETED("완료");

    private final String status;

    ProductStatus(String status){
        this.status = status;
    }
}
