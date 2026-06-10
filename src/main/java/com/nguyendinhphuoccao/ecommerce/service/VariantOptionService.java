package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.VariantOption;
import java.util.List;
import java.util.UUID;

public interface VariantOptionService {
    VariantOption create(VariantOption entity);
    VariantOption update(UUID id, VariantOption entity);
    void delete(UUID id);
    VariantOption getById(UUID id);
    List<VariantOption> getAll();
    void generateDummyVariants();
}
