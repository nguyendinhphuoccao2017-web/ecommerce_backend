package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.entity.Card;
import java.util.List;
import java.util.UUID;

import com.nguyendinhphuoccao.ecommerce.dto.cart.AddToCartRequestDTO;

public interface CardService {
    Card create(Card entity);
    Card update(UUID id, Card entity);
    void delete(UUID id);
    Card getById(UUID id);
    List<Card> getAll();
    void addToCart(UUID customerId, AddToCartRequestDTO request);
    com.nguyendinhphuoccao.ecommerce.dto.cart.CartResponseDTO getCart(UUID customerId);
}
