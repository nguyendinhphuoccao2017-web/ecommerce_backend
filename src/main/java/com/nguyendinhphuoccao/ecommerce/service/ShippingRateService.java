package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.ShippingRate;
import java.util.List;
import java.util.UUID;

public interface ShippingRateService {
    ShippingRate create(ShippingRate entity);
    ShippingRate update(UUID id, ShippingRate entity);
    void delete(UUID id);
    ShippingRate getById(UUID id);
    List<ShippingRate> getAll();
}
