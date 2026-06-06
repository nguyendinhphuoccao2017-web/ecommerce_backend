package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.ShippingZone;
import com.nguyendinhphuoccao.ecommerce.service.ShippingZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shipping-zones")
@RequiredArgsConstructor
public class ShippingZoneController {

    private final ShippingZoneService service;

    @PostMapping
    public ResponseEntity<ShippingZone> create(@RequestBody ShippingZone entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingZone> update(@PathVariable UUID id, @RequestBody ShippingZone entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingZone> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ShippingZone>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
