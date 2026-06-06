package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.ProductReview;
import com.nguyendinhphuoccao.ecommerce.repository.ProductReviewRepository;
import com.nguyendinhphuoccao.ecommerce.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository repository;

    @Override
    public ProductReview create(ProductReview entity) {
        return repository.save(entity);
    }

    @Override
    public ProductReview update(UUID id, ProductReview entity) {
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
    public ProductReview getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReview> getAll() {
        return repository.findAll();
    }
}
