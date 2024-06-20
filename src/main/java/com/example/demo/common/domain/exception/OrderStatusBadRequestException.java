package com.example.demo.common.domain.exception;

import com.example.demo.order.domain.OrderStatus;

public class OrderStatusBadRequestException extends RuntimeException {

    // product
    public OrderStatusBadRequestException() {
        super("요청 주문 상태가 잘못되었습니다.");
    }
    public OrderStatusBadRequestException(OrderStatus orderStatus, OrderStatus requestStatus) {
        super(orderStatus + "에서 " + requestStatus + "가 될 수 없습니다.");
    }
}
