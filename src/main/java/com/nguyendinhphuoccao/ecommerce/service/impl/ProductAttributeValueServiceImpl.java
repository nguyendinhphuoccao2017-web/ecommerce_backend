package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.ProductAttributeValue;
import com.nguyendinhphuoccao.ecommerce.repository.ProductAttributeValueRepository;
import com.nguyendinhphuoccao.ecommerce.service.ProductAttributeValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductAttributeValueServiceImpl implements ProductAttributeValueService {

    private final ProductAttributeValueRepository repository;

    @Override
    public ProductAttributeValue create(ProductAttributeValue entity) {
        return repository.save(entity);
    }

    @Override
    public ProductAttributeValue update(UUID id, ProductAttributeValue entity) {
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
    public ProductAttributeValue getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductAttributeValue> getAll() {
        return repository.findAll();
    }
}
