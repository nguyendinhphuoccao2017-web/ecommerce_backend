package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.ProductSupplier;
import java.util.List;
import java.util.UUID;

public interface ProductSupplierService {
    ProductSupplier create(ProductSupplier entity);
    ProductSupplier update(UUID id, ProductSupplier entity);
    void delete(UUID id);
    ProductSupplier getById(UUID id);
    List<ProductSupplier> getAll();
}
