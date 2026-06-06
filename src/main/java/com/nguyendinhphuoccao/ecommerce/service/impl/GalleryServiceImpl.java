package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.Gallery;
import com.nguyendinhphuoccao.ecommerce.repository.GalleryRepository;
import com.nguyendinhphuoccao.ecommerce.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository repository;

    @Override
    public Gallery create(Gallery entity) {
        return repository.save(entity);
    }

    @Override
    public Gallery update(UUID id, Gallery entity) {
        if(repository.existsById(id)) {
            entity.setId(id);
            return repository.save(entity);
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Gallery getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Gallery> getAll() {
        return repository.findAll();
    }
}
