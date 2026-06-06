package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Order;
import java.util.List;

public interface OrderService {
    Order create(Order entity);
    Order update(String id, Order entity);
    void delete(String id);
    Order getById(String id);
    List<Order> getAll();
}
