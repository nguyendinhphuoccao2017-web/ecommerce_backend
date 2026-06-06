package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.ProductCategory;
import java.util.List;
import java.util.UUID;

public interface ProductCategoryService {
    ProductCategory create(ProductCategory entity);
    ProductCategory update(UUID id, ProductCategory entity);
    void delete(UUID id);
    ProductCategory getById(UUID id);
    List<ProductCategory> getAll();
}
