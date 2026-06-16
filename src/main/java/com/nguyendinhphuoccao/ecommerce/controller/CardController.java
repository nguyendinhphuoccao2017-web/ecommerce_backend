package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.Card;
import com.nguyendinhphuoccao.ecommerce.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService service;

    @PostMapping
    public ResponseEntity<Card> create(@RequestBody Card entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(
            @RequestBody com.nguyendinhphuoccao.ecommerce.dto.cart.AddToCartRequestDTO request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.nguyendinhphuoccao.ecommerce.security.CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getCustomer() == null || userDetails.getCustomer().getId() == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        service.addToCart(userDetails.getCustomer().getId(), request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> update(@PathVariable UUID id, @RequestBody Card entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Card>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
