package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.ProductReview;
import java.util.List;
import java.util.UUID;

public interface ProductReviewService {
    ProductReview create(ProductReview entity);
    ProductReview update(UUID id, ProductReview entity);
    void delete(UUID id);
    ProductReview getById(UUID id);
    List<ProductReview> getAll();
}
