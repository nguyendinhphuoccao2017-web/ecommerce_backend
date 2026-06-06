package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Tag;
import java.util.List;
import java.util.UUID;

public interface TagService {
    Tag create(Tag entity);
    Tag update(UUID id, Tag entity);
    void delete(UUID id);
    Tag getById(UUID id);
    List<Tag> getAll();
}
