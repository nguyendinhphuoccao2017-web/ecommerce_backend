package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.ProductShippingInfo;
import java.util.List;
import java.util.UUID;

public interface ProductShippingInfoService {
    ProductShippingInfo create(ProductShippingInfo entity);
    ProductShippingInfo update(UUID id, ProductShippingInfo entity);
    void delete(UUID id);
    ProductShippingInfo getById(UUID id);
    List<ProductShippingInfo> getAll();
}
