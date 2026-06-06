package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.ProductAttributeValue;
import com.nguyendinhphuoccao.ecommerce.service.ProductAttributeValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-attribute-values")
@RequiredArgsConstructor
public class ProductAttributeValueController {

    private final ProductAttributeValueService service;

    @PostMapping
    public ResponseEntity<ProductAttributeValue> create(@RequestBody ProductAttributeValue entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductAttributeValue> update(@PathVariable UUID id, @RequestBody ProductAttributeValue entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductAttributeValue> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductAttributeValue>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
