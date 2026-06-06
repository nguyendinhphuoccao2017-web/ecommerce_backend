package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.CardItem;
import com.nguyendinhphuoccao.ecommerce.service.CardItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/card-items")
@RequiredArgsConstructor
public class CardItemController {

    private final CardItemService service;

    @PostMapping
    public ResponseEntity<CardItem> create(@RequestBody CardItem entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardItem> update(@PathVariable UUID id, @RequestBody CardItem entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardItem> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CardItem>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
