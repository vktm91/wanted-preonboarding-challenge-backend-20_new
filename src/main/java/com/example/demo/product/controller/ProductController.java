package com.example.demo.product.controller;

import com.example.demo.product.controller.port.ProductService;
import com.example.demo.product.controller.response.ProductResponse;
import com.example.demo.product.domain.ProductCreate;
import com.example.demo.product.domain.ProductUpdate;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "상품(products)")
@RestController
@Builder
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreate postCreateDto) {
        ProductResponse productResponse = ProductResponse.from(productService.create(postCreateDto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productResponse.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(productResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable long id, @RequestParam(required = false) Long userId) {
        return ResponseEntity
                .ok()
                .body(productService.getByIdAndUserId(id, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateInfo(@PathVariable long id, @Valid @RequestBody ProductUpdate postUpdateDto) {
        return ResponseEntity
                .ok()
                .body(ProductResponse.from(productService.updateInfo(id, postUpdateDto)));
    }
}
