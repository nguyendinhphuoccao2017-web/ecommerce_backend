package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.VariantValue;
import java.util.List;
import java.util.UUID;

public interface VariantValueService {
    VariantValue create(VariantValue entity);
    VariantValue update(UUID id, VariantValue entity);
    void delete(UUID id);
    VariantValue getById(UUID id);
    List<VariantValue> getAll();
}
