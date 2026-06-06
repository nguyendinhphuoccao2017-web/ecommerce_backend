package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.Slideshow;
import com.nguyendinhphuoccao.ecommerce.repository.SlideshowRepository;
import com.nguyendinhphuoccao.ecommerce.service.SlideshowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SlideshowServiceImpl implements SlideshowService {

    private final SlideshowRepository repository;

    @Override
    public Slideshow create(Slideshow entity) {
        return repository.save(entity);
    }

    @Override
    public Slideshow update(UUID id, Slideshow entity) {
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
    public Slideshow getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Slideshow> getAll() {
        return repository.findAll();
    }
}
