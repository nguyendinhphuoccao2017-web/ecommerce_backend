package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO;
import com.nguyendinhphuoccao.ecommerce.service.CustomerFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class CustomerFavoriteController {

    private final CustomerFavoriteService service;

    @GetMapping
    public ResponseEntity<List<ProductCategoryResponseDTO>> getFavoriteProducts() {
        return ResponseEntity.ok(service.getFavoriteProducts());
    }

    @PostMapping("/{productId}/toggle")
    public ResponseEntity<Void> toggleFavorite(@PathVariable UUID productId) {
        service.toggleFavorite(productId);
        return ResponseEntity.ok().build();
    }
}
