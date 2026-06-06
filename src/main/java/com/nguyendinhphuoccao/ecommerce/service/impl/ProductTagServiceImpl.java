package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.ProductTag;
import com.nguyendinhphuoccao.ecommerce.repository.ProductTagRepository;
import com.nguyendinhphuoccao.ecommerce.service.ProductTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductTagServiceImpl implements ProductTagService {

    private final ProductTagRepository repository;

    @Override
    public ProductTag create(ProductTag entity) {
        return repository.save(entity);
    }

    @Override
    public ProductTag update(UUID id, ProductTag entity) {
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
    public ProductTag getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductTag> getAll() {
        return repository.findAll();
    }
}
