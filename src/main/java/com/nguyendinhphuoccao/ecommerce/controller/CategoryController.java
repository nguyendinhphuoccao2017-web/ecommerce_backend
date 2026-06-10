package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.Category;
import com.nguyendinhphuoccao.ecommerce.service.CategoryService;
import com.nguyendinhphuoccao.ecommerce.service.ProductService;
import com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable UUID id, @RequestBody Category entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductCategoryResponseDTO>> getProductsByCategory(
            @PathVariable UUID id,
            @RequestParam(required = false) String tagName) {
        return ResponseEntity.ok(productService.getProductsByCategory(id, tagName));
    }
}
