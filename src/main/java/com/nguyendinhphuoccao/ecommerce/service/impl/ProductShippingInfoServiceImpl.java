package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.ProductShippingInfo;
import com.nguyendinhphuoccao.ecommerce.repository.ProductShippingInfoRepository;
import com.nguyendinhphuoccao.ecommerce.service.ProductShippingInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductShippingInfoServiceImpl implements ProductShippingInfoService {

    private final ProductShippingInfoRepository repository;

    @Override
    public ProductShippingInfo create(ProductShippingInfo entity) {
        return repository.save(entity);
    }

    @Override
    public ProductShippingInfo update(UUID id, ProductShippingInfo entity) {
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
    public ProductShippingInfo getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductShippingInfo> getAll() {
        return repository.findAll();
    }
}
