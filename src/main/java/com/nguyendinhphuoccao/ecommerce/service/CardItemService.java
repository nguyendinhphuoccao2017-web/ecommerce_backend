package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.CardItem;
import java.util.List;
import java.util.UUID;

public interface CardItemService {
    CardItem create(CardItem entity);
    CardItem update(UUID id, CardItem entity);
    void delete(UUID id);
    CardItem getById(UUID id);
    List<CardItem> getAll();
}
