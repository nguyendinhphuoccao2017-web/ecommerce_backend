package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.StaffAccount;
import com.nguyendinhphuoccao.ecommerce.service.StaffAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/staff-accounts")
@RequiredArgsConstructor
public class StaffAccountController {

    private final StaffAccountService service;

    @PostMapping
    public ResponseEntity<StaffAccount> create(@RequestBody StaffAccount entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffAccount> update(@PathVariable UUID id, @RequestBody StaffAccount entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffAccount> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<StaffAccount>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
