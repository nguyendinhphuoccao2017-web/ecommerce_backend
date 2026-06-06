package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.ProductAttribute;
import java.util.List;
import java.util.UUID;

public interface ProductAttributeService {
    ProductAttribute create(ProductAttribute entity);
    ProductAttribute update(UUID id, ProductAttribute entity);
    void delete(UUID id);
    ProductAttribute getById(UUID id);
    List<ProductAttribute> getAll();
}
