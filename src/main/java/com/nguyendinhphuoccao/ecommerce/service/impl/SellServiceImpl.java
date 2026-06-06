package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.Sell;
import com.nguyendinhphuoccao.ecommerce.repository.SellRepository;
import com.nguyendinhphuoccao.ecommerce.service.SellService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SellServiceImpl implements SellService {

    private final SellRepository repository;

    @Override
    public Sell create(Sell entity) {
        return repository.save(entity);
    }

    @Override
    public Sell update(UUID id, Sell entity) {
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
    public Sell getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sell> getAll() {
        return repository.findAll();
    }
}
