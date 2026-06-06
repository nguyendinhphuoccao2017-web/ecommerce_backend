package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.VariantValue;
import com.nguyendinhphuoccao.ecommerce.repository.VariantValueRepository;
import com.nguyendinhphuoccao.ecommerce.service.VariantValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VariantValueServiceImpl implements VariantValueService {

    private final VariantValueRepository repository;

    @Override
    public VariantValue create(VariantValue entity) {
        return repository.save(entity);
    }

    @Override
    public VariantValue update(UUID id, VariantValue entity) {
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
    public VariantValue getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VariantValue> getAll() {
        return repository.findAll();
    }
}
