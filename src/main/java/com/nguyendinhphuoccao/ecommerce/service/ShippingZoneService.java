package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.ShippingZone;
import java.util.List;
import java.util.UUID;

public interface ShippingZoneService {
    ShippingZone create(ShippingZone entity);
    ShippingZone update(UUID id, ShippingZone entity);
    void delete(UUID id);
    ShippingZone getById(UUID id);
    List<ShippingZone> getAll();
}
