package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.ProductCoupon;
import java.util.List;
import java.util.UUID;

public interface ProductCouponService {
    ProductCoupon create(ProductCoupon entity);
    ProductCoupon update(UUID id, ProductCoupon entity);
    void delete(UUID id);
    ProductCoupon getById(UUID id);
    List<ProductCoupon> getAll();
}
