package com.example.demo.order.controller;

import com.example.demo.order.controller.port.OrderHistoryService;
import com.example.demo.order.controller.response.OrderHistoriesResponse;
import com.example.demo.order.domain.OrderHistoryCreate;
import com.example.demo.order.domain.OrderHistoryUpdate;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@Builder
@RequiredArgsConstructor
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;

    @PostMapping
    public ResponseEntity<OrderHistoriesResponse> create(@Valid @RequestBody OrderHistoryCreate orderHistoryCreate) {
        OrderHistoriesResponse orderHistoryResponse = OrderHistoriesResponse.from(orderHistoryService.createOrder(orderHistoryCreate.getBuyerId(), orderHistoryCreate.getProductId()));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderHistoryResponse.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(orderHistoryResponse);
    }

    @PatchMapping
    public ResponseEntity<OrderHistoriesResponse> update(@Valid @RequestBody OrderHistoryUpdate orderHistoryUpdate) {
        return ResponseEntity
                .ok()
                .body(OrderHistoriesResponse.from(orderHistoryService.updateStatus(orderHistoryUpdate)));
    }

}
