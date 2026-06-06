package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.ProductTag;
import java.util.List;
import java.util.UUID;

public interface ProductTagService {
    ProductTag create(ProductTag entity);
    ProductTag update(UUID id, ProductTag entity);
    void delete(UUID id);
    ProductTag getById(UUID id);
    List<ProductTag> getAll();
}
