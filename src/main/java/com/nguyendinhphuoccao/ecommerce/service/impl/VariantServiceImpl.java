package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.Variant;
import com.nguyendinhphuoccao.ecommerce.repository.VariantRepository;
import com.nguyendinhphuoccao.ecommerce.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VariantServiceImpl implements VariantService {

    private final VariantRepository repository;

    @Override
    public Variant create(Variant entity) {
        return repository.save(entity);
    }

    @Override
    public Variant update(UUID id, Variant entity) {
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
    public Variant getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Variant> getAll() {
        return repository.findAll();
    }
}
