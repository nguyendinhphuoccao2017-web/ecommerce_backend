package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.ProductAttributeValue;
import java.util.List;
import java.util.UUID;

public interface ProductAttributeValueService {
    ProductAttributeValue create(ProductAttributeValue entity);
    ProductAttributeValue update(UUID id, ProductAttributeValue entity);
    void delete(UUID id);
    ProductAttributeValue getById(UUID id);
    List<ProductAttributeValue> getAll();
}
