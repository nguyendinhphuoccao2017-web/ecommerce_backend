package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Variant;
import java.util.List;
import java.util.UUID;

public interface VariantService {
    Variant create(Variant entity);
    Variant update(UUID id, Variant entity);
    void delete(UUID id);
    Variant getById(UUID id);
    List<Variant> getAll();
}
