package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.ProductSupplier;
import com.nguyendinhphuoccao.ecommerce.repository.ProductSupplierRepository;
import com.nguyendinhphuoccao.ecommerce.service.ProductSupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductSupplierServiceImpl implements ProductSupplierService {

    private final ProductSupplierRepository repository;

    @Override
    public ProductSupplier create(ProductSupplier entity) {
        return repository.save(entity);
    }

    @Override
    public ProductSupplier update(UUID id, ProductSupplier entity) {
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
    public ProductSupplier getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSupplier> getAll() {
        return repository.findAll();
    }
}
