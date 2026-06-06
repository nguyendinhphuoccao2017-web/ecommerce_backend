package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Supplier;
import java.util.List;
import java.util.UUID;

public interface SupplierService {
    Supplier create(Supplier entity);
    Supplier update(UUID id, Supplier entity);
    void delete(UUID id);
    Supplier getById(UUID id);
    List<Supplier> getAll();
}
