package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.AttributeValue;
import com.nguyendinhphuoccao.ecommerce.repository.AttributeValueRepository;
import com.nguyendinhphuoccao.ecommerce.service.AttributeValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AttributeValueServiceImpl implements AttributeValueService {

    private final AttributeValueRepository repository;

    @Override
    public AttributeValue create(AttributeValue entity) {
        return repository.save(entity);
    }

    @Override
    public AttributeValue update(UUID id, AttributeValue entity) {
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
    public AttributeValue getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttributeValue> getAll() {
        return repository.findAll();
    }
}
