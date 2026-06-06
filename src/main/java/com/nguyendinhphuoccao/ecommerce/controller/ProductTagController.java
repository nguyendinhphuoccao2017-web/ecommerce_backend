package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.ProductTag;
import com.nguyendinhphuoccao.ecommerce.service.ProductTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-tags")
@RequiredArgsConstructor
public class ProductTagController {

    private final ProductTagService service;

    @PostMapping
    public ResponseEntity<ProductTag> create(@RequestBody ProductTag entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductTag> update(@PathVariable UUID id, @RequestBody ProductTag entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTag> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductTag>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
