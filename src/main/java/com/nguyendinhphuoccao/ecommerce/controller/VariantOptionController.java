package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.VariantOption;
import com.nguyendinhphuoccao.ecommerce.service.VariantOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/variant-options")
@RequiredArgsConstructor
public class VariantOptionController {

    private final VariantOptionService service;

    @PostMapping
    public ResponseEntity<VariantOption> create(@RequestBody VariantOption entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariantOption> update(@PathVariable UUID id, @RequestBody VariantOption entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariantOption> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<VariantOption>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
