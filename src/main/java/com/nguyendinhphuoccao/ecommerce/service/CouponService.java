package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Coupon;
import java.util.List;
import java.util.UUID;

public interface CouponService {
    Coupon create(Coupon entity);
    Coupon update(UUID id, Coupon entity);
    void delete(UUID id);
    Coupon getById(UUID id);
    List<Coupon> getAll();
}
