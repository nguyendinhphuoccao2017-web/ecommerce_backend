package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.ProductCoupon;
import com.nguyendinhphuoccao.ecommerce.repository.ProductCouponRepository;
import com.nguyendinhphuoccao.ecommerce.service.ProductCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCouponServiceImpl implements ProductCouponService {

    private final ProductCouponRepository repository;

    @Override
    public ProductCoupon create(ProductCoupon entity) {
        return repository.save(entity);
    }

    @Override
    public ProductCoupon update(UUID id, ProductCoupon entity) {
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
    public ProductCoupon getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCoupon> getAll() {
        return repository.findAll();
    }
}
