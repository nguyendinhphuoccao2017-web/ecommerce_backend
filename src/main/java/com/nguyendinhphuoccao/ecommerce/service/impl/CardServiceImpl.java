package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.Card;
import com.nguyendinhphuoccao.ecommerce.repository.CardRepository;
import com.nguyendinhphuoccao.ecommerce.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository repository;

    @Override
    public Card create(Card entity) {
        return repository.save(entity);
    }

    @Override
    public Card update(UUID id, Card entity) {
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
    public Card getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> getAll() {
        return repository.findAll();
    }
}
