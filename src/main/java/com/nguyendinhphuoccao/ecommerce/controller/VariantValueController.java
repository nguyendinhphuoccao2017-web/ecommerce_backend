package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.VariantValue;
import com.nguyendinhphuoccao.ecommerce.service.VariantValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/variant-values")
@RequiredArgsConstructor
public class VariantValueController {

    private final VariantValueService service;

    @PostMapping
    public ResponseEntity<VariantValue> create(@RequestBody VariantValue entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariantValue> update(@PathVariable UUID id, @RequestBody VariantValue entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariantValue> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<VariantValue>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
