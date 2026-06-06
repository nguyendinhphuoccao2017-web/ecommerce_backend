package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.VariantOption;
import com.nguyendinhphuoccao.ecommerce.repository.VariantOptionRepository;
import com.nguyendinhphuoccao.ecommerce.service.VariantOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VariantOptionServiceImpl implements VariantOptionService {

    private final VariantOptionRepository repository;

    @Override
    public VariantOption create(VariantOption entity) {
        return repository.save(entity);
    }

    @Override
    public VariantOption update(UUID id, VariantOption entity) {
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
    public VariantOption getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VariantOption> getAll() {
        return repository.findAll();
    }
}
