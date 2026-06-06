package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.ShippingRate;
import com.nguyendinhphuoccao.ecommerce.service.ShippingRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shipping-rates")
@RequiredArgsConstructor
public class ShippingRateController {

    private final ShippingRateService service;

    @PostMapping
    public ResponseEntity<ShippingRate> create(@RequestBody ShippingRate entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingRate> update(@PathVariable UUID id, @RequestBody ShippingRate entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingRate> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ShippingRate>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
