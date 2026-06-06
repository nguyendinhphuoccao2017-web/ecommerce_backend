package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Attribute;
import java.util.List;
import java.util.UUID;

public interface AttributeService {
    Attribute create(Attribute entity);
    Attribute update(UUID id, Attribute entity);
    void delete(UUID id);
    Attribute getById(UUID id);
    List<Attribute> getAll();
}
