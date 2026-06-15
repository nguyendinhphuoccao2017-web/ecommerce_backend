package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.Product;
import com.nguyendinhphuoccao.ecommerce.service.ProductService;
import com.nguyendinhphuoccao.ecommerce.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import com.nguyendinhphuoccao.ecommerce.dto.product.ProductRequestDTO;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final ProductReviewService reviewService;

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductRequestDTO request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable UUID id, @RequestBody ProductRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/home/tags/{tagName}")
    public ResponseEntity<List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductHomeResponseDTO>> getHomeProductsByTag(@PathVariable String tagName) {
        return ResponseEntity.ok(service.getProductsByTag(tagName));
    }

    @GetMapping("/{id}/variants")
    public ResponseEntity<List<com.nguyendinhphuoccao.ecommerce.dto.product.VariantOptionDTO>> getVariants(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getVariantsByProductId(id));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<com.nguyendinhphuoccao.ecommerce.dto.product.ProductDetailResponseDTO> getDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getProductDetails(id));
    }

    @GetMapping("/{id}/related")
    public ResponseEntity<List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductHomeResponseDTO>> getRelatedProducts(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getRelatedProducts(id));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<org.springframework.data.domain.Page<com.nguyendinhphuoccao.ecommerce.dto.review.ReviewResponseDTO>> getProductReviews(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "false") boolean withPhoto,
            org.springframework.data.domain.Pageable pageable) {
        return ResponseEntity.ok(reviewService.getProductReviews(id, withPhoto, pageable));
    }

    @GetMapping("/{id}/reviews/summary")
    public ResponseEntity<com.nguyendinhphuoccao.ecommerce.dto.review.ReviewSummaryDTO> getProductReviewSummary(@PathVariable UUID id) {
        return ResponseEntity.ok(reviewService.getProductReviewSummary(id));
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<com.nguyendinhphuoccao.ecommerce.entity.ProductReview> addReview(
            @PathVariable UUID id,
            @RequestBody com.nguyendinhphuoccao.ecommerce.dto.review.ReviewRequestDTO request) {
        return ResponseEntity.ok(reviewService.addReview(id, request));
    }
}
