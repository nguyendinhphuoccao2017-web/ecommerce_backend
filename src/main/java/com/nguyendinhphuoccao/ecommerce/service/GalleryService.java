package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Gallery;
import java.util.List;
import java.util.UUID;

public interface GalleryService {
    Gallery create(Gallery entity);
    Gallery update(UUID id, Gallery entity);
    void delete(UUID id);
    Gallery getById(UUID id);
    List<Gallery> getAll();
}
