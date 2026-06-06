package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.OrderStatus;
import com.nguyendinhphuoccao.ecommerce.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-statuses")
@RequiredArgsConstructor
public class OrderStatusController {

    private final OrderStatusService service;

    @PostMapping
    public ResponseEntity<OrderStatus> create(@RequestBody OrderStatus entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderStatus> update(@PathVariable UUID id, @RequestBody OrderStatus entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderStatus> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderStatus>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
