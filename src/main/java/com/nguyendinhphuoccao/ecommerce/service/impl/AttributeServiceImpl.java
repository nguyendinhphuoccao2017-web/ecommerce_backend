package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.Attribute;
import com.nguyendinhphuoccao.ecommerce.repository.AttributeRepository;
import com.nguyendinhphuoccao.ecommerce.service.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository repository;

    @Override
    public Attribute create(Attribute entity) {
        return repository.save(entity);
    }

    @Override
    public Attribute update(UUID id, Attribute entity) {
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
    public Attribute getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attribute> getAll() {
        return repository.findAll();
    }
}
