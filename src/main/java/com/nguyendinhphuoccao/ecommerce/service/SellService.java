package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Sell;
import java.util.List;
import java.util.UUID;

public interface SellService {
    Sell create(Sell entity);
    Sell update(UUID id, Sell entity);
    void delete(UUID id);
    Sell getById(UUID id);
    List<Sell> getAll();
}
