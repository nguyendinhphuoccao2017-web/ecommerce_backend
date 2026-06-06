package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product create(Product entity);
    Product update(UUID id, Product entity);
    void delete(UUID id);
    Product getById(UUID id);
    List<Product> getAll();
}
