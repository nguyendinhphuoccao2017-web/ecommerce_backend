package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.ProductAttribute;
import com.nguyendinhphuoccao.ecommerce.repository.ProductAttributeRepository;
import com.nguyendinhphuoccao.ecommerce.service.ProductAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductAttributeServiceImpl implements ProductAttributeService {

    private final ProductAttributeRepository repository;

    @Override
    public ProductAttribute create(ProductAttribute entity) {
        return repository.save(entity);
    }

    @Override
    public ProductAttribute update(UUID id, ProductAttribute entity) {
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
    public ProductAttribute getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductAttribute> getAll() {
        return repository.findAll();
    }
}
