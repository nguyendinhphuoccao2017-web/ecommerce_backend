package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Product;
import java.util.List;
import java.util.UUID;

import com.nguyendinhphuoccao.ecommerce.dto.product.ProductRequestDTO;

public interface ProductService {
    Product create(ProductRequestDTO request);
    Product update(UUID id, ProductRequestDTO request);
    void delete(UUID id);
    Product getById(UUID id);
    List<Product> getAll();
    List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductHomeResponseDTO> getProductsByTag(String tagName);
    List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO> getProductsByCategory(UUID categoryId);
}
