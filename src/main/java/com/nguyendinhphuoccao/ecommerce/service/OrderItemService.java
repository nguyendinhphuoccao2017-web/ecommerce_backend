package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.OrderItem;
import java.util.List;
import java.util.UUID;

public interface OrderItemService {
    OrderItem create(OrderItem entity);
    OrderItem update(UUID id, OrderItem entity);
    void delete(UUID id);
    OrderItem getById(UUID id);
    List<OrderItem> getAll();
}
