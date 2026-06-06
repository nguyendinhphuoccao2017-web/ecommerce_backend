package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.OrderStatus;
import java.util.List;
import java.util.UUID;

public interface OrderStatusService {
    OrderStatus create(OrderStatus entity);
    OrderStatus update(UUID id, OrderStatus entity);
    void delete(UUID id);
    OrderStatus getById(UUID id);
    List<OrderStatus> getAll();
}
