package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.ProductCoupon;
import com.nguyendinhphuoccao.ecommerce.service.ProductCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-coupons")
@RequiredArgsConstructor
public class ProductCouponController {

    private final ProductCouponService service;

    @PostMapping
    public ResponseEntity<ProductCoupon> create(@RequestBody ProductCoupon entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCoupon> update(@PathVariable UUID id, @RequestBody ProductCoupon entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCoupon> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductCoupon>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
