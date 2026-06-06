package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.OrderStatus;
import com.nguyendinhphuoccao.ecommerce.repository.OrderStatusRepository;
import com.nguyendinhphuoccao.ecommerce.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderStatusRepository repository;

    @Override
    public OrderStatus create(OrderStatus entity) {
        return repository.save(entity);
    }

    @Override
    public OrderStatus update(UUID id, OrderStatus entity) {
        if(repository.existsById(id)) {
            entity.setId(id);
            return repository.save(entity);
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderStatus getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderStatus> getAll() {
        return repository.findAll();
    }
}
