package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.Order;
import com.nguyendinhphuoccao.ecommerce.repository.OrderRepository;
import com.nguyendinhphuoccao.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    @Override
    public Order create(Order entity) {
        return repository.save(entity);
    }

    @Override
    public Order update(String id, Order entity) {
        if(repository.existsById(id)) {
            entity.setId(id);
            return repository.save(entity);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAll() {
        return repository.findAll();
    }
}
