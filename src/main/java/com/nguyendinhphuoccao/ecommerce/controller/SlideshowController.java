package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.Slideshow;
import com.nguyendinhphuoccao.ecommerce.service.SlideshowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/slideshows")
@RequiredArgsConstructor
public class SlideshowController {

    private final SlideshowService service;

    @PostMapping
    public ResponseEntity<Slideshow> create(@RequestBody Slideshow entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Slideshow> update(@PathVariable UUID id, @RequestBody Slideshow entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Slideshow> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Slideshow>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/home")
    public ResponseEntity<List<Slideshow>> getHomeSlideshows() {
        return ResponseEntity.ok(service.getHomeSlideshows());
    }
}
