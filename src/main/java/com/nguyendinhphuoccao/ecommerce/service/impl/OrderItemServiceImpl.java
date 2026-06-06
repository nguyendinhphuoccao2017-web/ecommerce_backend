package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.OrderItem;
import com.nguyendinhphuoccao.ecommerce.repository.OrderItemRepository;
import com.nguyendinhphuoccao.ecommerce.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository repository;

    @Override
    public OrderItem create(OrderItem entity) {
        return repository.save(entity);
    }

    @Override
    public OrderItem update(UUID id, OrderItem entity) {
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
    public OrderItem getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> getAll() {
        return repository.findAll();
    }
}
