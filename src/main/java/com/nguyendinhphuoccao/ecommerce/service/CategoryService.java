package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Category;
import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category create(Category entity);
    Category update(UUID id, Category entity);
    void delete(UUID id);
    Category getById(UUID id);
    List<Category> getAll();
}
