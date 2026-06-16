package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.Card;
import com.nguyendinhphuoccao.ecommerce.entity.CardItem;
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
    private final com.nguyendinhphuoccao.ecommerce.repository.CustomerRepository customerRepository;
    private final com.nguyendinhphuoccao.ecommerce.repository.ProductRepository productRepository;
    private final com.nguyendinhphuoccao.ecommerce.repository.CardItemRepository cardItemRepository;

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

    @Override
    public void addToCart(UUID customerId, com.nguyendinhphuoccao.ecommerce.dto.cart.AddToCartRequestDTO request) {
        com.nguyendinhphuoccao.ecommerce.entity.Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Card card = repository.findByCustomerId(customerId).orElseGet(() -> {
            Card newCard = new Card();
            newCard.setCustomer(customer);
            return repository.save(newCard);
        });

        com.nguyendinhphuoccao.ecommerce.entity.Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int stock = product.getQuantity() != null ? product.getQuantity() : 0;
        int requestQty = request.getQuantity() != null ? request.getQuantity() : 1;

        com.nguyendinhphuoccao.ecommerce.entity.CardItem cardItem = cardItemRepository.findByCardIdAndProductId(card.getId(), product.getId())
                .orElse(null);

        int currentQty = (cardItem != null && cardItem.getQuantity() != null) ? cardItem.getQuantity() : 0;
        int newTotalQty = currentQty + requestQty;

        if (newTotalQty > stock) {
            throw new RuntimeException("Not enough stock. Available: " + stock + ", in cart: " + currentQty + ", requested: " + requestQty);
        }

        if (cardItem != null) {
            cardItem.setQuantity(newTotalQty);
            cardItemRepository.save(cardItem);
        } else {
            CardItem newItem = new CardItem();
            newItem.setCard(card);
            newItem.setProduct(product);
            newItem.setQuantity(requestQty);
            cardItemRepository.save(newItem);
        }
    }
}
