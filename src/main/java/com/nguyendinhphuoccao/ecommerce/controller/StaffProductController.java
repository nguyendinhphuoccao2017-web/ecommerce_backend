package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.dto.product.ProductRequestDTO;
import com.nguyendinhphuoccao.ecommerce.entity.Product;
import com.nguyendinhphuoccao.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/staff/products")
@RequiredArgsConstructor
public class StaffProductController {
    
    private final ProductService service;

    @PostMapping
    public ResponseEntity<List<Product>> createBatch(@RequestBody List<ProductRequestDTO> requests) {
        List<Product> products = new ArrayList<>();
        for (ProductRequestDTO request : requests) {
            products.add(service.create(request));
        }
        return ResponseEntity.ok(products);
    }
}
