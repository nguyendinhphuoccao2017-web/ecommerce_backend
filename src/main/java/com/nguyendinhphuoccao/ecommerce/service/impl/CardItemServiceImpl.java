package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.CardItem;
import com.nguyendinhphuoccao.ecommerce.repository.CardItemRepository;
import com.nguyendinhphuoccao.ecommerce.service.CardItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CardItemServiceImpl implements CardItemService {

    private final CardItemRepository repository;

    @Override
    public CardItem create(CardItem entity) {
        return repository.save(entity);
    }

    @Override
    public CardItem update(UUID id, CardItem entity) {
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
    public CardItem getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardItem> getAll() {
        return repository.findAll();
    }
}
