package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.AttributeValue;
import java.util.List;
import java.util.UUID;

public interface AttributeValueService {
    AttributeValue create(AttributeValue entity);
    AttributeValue update(UUID id, AttributeValue entity);
    void delete(UUID id);
    AttributeValue getById(UUID id);
    List<AttributeValue> getAll();
}
