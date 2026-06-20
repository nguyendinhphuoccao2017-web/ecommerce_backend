package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.Card;
import com.nguyendinhphuoccao.ecommerce.entity.CardItem;
import com.nguyendinhphuoccao.ecommerce.repository.CardRepository;
import com.nguyendinhphuoccao.ecommerce.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository repository;
    private final com.nguyendinhphuoccao.ecommerce.repository.CustomerRepository customerRepository;
    private final com.nguyendinhphuoccao.ecommerce.repository.ProductRepository productRepository;
    private final com.nguyendinhphuoccao.ecommerce.repository.CardItemRepository cardItemRepository;
    private final com.nguyendinhphuoccao.ecommerce.repository.VariantOptionRepository variantOptionRepository;

    private static final Map<UUID, UUID> cartVariantMap = new ConcurrentHashMap<>();

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

        com.nguyendinhphuoccao.ecommerce.entity.VariantOption variantOption = null;
        if (request.getVariantOptionId() != null) {
            variantOption = variantOptionRepository.findById(request.getVariantOptionId())
                    .orElseThrow(() -> new RuntimeException("Variant not found"));
            stock = variantOption.getQuantity() != null ? variantOption.getQuantity() : 0;
        }

        com.nguyendinhphuoccao.ecommerce.entity.CardItem cardItem = cardItemRepository
                .findByCardIdAndProductIdAndVariantOption(card.getId(), product.getId(), request.getVariantOptionId())
                .orElse(null);

        int currentQty = (cardItem != null && cardItem.getQuantity() != null) ? cardItem.getQuantity() : 0;
        int newTotalQty = currentQty + requestQty;

        if (newTotalQty > stock) {
            throw new RuntimeException("Not enough stock. Available: " + stock + ", in cart: " + currentQty + ", requested: " + requestQty);
        }

        if (cardItem != null) {
            cardItem.setQuantity(newTotalQty);
            cardItemRepository.save(cardItem);
            if (request.getVariantOptionId() != null) {
                cartVariantMap.put(cardItem.getId(), request.getVariantOptionId());
            }
        } else {
            CardItem newItem = new CardItem();
            newItem.setCard(card);
            newItem.setProduct(product);
            newItem.setVariantOption(variantOption);
            newItem.setQuantity(requestQty);
            newItem = cardItemRepository.save(newItem);
            if (request.getVariantOptionId() != null) {
                cartVariantMap.put(newItem.getId(), request.getVariantOptionId());
            }
        }
    }

    @Override
    public void updateItemQuantity(UUID itemId, int quantity) {
        com.nguyendinhphuoccao.ecommerce.entity.CardItem cardItem = cardItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        int stock = cardItem.getProduct().getQuantity() != null ? cardItem.getProduct().getQuantity() : 0;
        if (cardItem.getVariantOption() != null) {
            stock = cardItem.getVariantOption().getQuantity() != null ? cardItem.getVariantOption().getQuantity() : 0;
        }

        if (quantity > stock) {
            throw new RuntimeException("Not enough stock. Available: " + stock + ", requested: " + quantity);
        }

        cardItem.setQuantity(quantity);
        cardItemRepository.save(cardItem);
    }

    @Override
    @Transactional(readOnly = true)
    public com.nguyendinhphuoccao.ecommerce.dto.cart.CartResponseDTO getCart(UUID customerId) {
        Card card = repository.findByCustomerId(customerId).orElse(null);
        if (card == null) {
            return com.nguyendinhphuoccao.ecommerce.dto.cart.CartResponseDTO.builder()
                    .items(java.util.Collections.emptyList())
                    .subtotal(java.math.BigDecimal.ZERO)
                    .build();
        }

        java.math.BigDecimal subtotal = java.math.BigDecimal.ZERO;
        List<com.nguyendinhphuoccao.ecommerce.dto.cart.CartItemDTO> itemDTOs = new java.util.ArrayList<>();

        if (card.getCardItems() != null) {
            for (CardItem item : card.getCardItems()) {
                com.nguyendinhphuoccao.ecommerce.entity.Product product = item.getProduct();
                if (product == null) continue;

                java.math.BigDecimal price = product.getSalePrice(); // Fallback to regular price if variants aren't used yet
                String variantTitle = null;
                Integer maxStock = product.getQuantity();
                UUID voId = cartVariantMap.get(item.getId());
                com.nguyendinhphuoccao.ecommerce.entity.VariantOption vo = null;
                if (voId != null) {
                    vo = variantOptionRepository.findById(voId).orElse(null);
                }

                if (vo != null) {
                    price = vo.getSalePrice() != null ? vo.getSalePrice() : vo.getComparePrice();
                    if (price == null) price = product.getSalePrice();
                    variantTitle = vo.getTitle();
                    maxStock = vo.getQuantity();
                }

                if (price == null) price = java.math.BigDecimal.ZERO;
                java.math.BigDecimal itemTotal = price.multiply(java.math.BigDecimal.valueOf(item.getQuantity()));
                subtotal = subtotal.add(itemTotal);

                String imageUrl = null;
                if (product.getGalleries() != null && !product.getGalleries().isEmpty()) {
                    imageUrl = product.getGalleries().stream().filter(g -> g.getIsThumbnail() != null && g.getIsThumbnail()).map(g -> g.getImage()).findFirst().orElse(null);
                    if (imageUrl == null) imageUrl = product.getGalleries().get(0).getImage();
                }

                String color = null;
                String size = null;
                if (variantTitle != null) {
                    String[] parts = variantTitle.split(", ");
                    if (parts.length >= 2) {
                        color = parts[0];
                        size = parts[1];
                    } else if (parts.length == 1) {
                        color = parts[0];
                    }
                } else if (product.getVariantOptions() != null && !product.getVariantOptions().isEmpty()) {
                    // Fallback An toàn: Không lấy get(0) để tránh mua nhầm
                    color = "Vui lòng chọn lại";
                    size = "Vui lòng chọn lại";
                }

                itemDTOs.add(com.nguyendinhphuoccao.ecommerce.dto.cart.CartItemDTO.builder()
                        .id(item.getId())
                        .productId(product.getId())
                        .productName(product.getProductName())
                        .sku(item.getVariantOption() != null ? item.getVariantOption().getSku() : product.getSku())
                        .salePrice(price)
                        .image(imageUrl)
                        .variantOptionId(voId)
                        .variantTitle(variantTitle)
                        .color(color)
                        .size(size)
                        .quantity(item.getQuantity())
                        .maxQuantity(maxStock)
                        .build());
            }
        }

        return com.nguyendinhphuoccao.ecommerce.dto.cart.CartResponseDTO.builder()
                .cardId(card.getId())
                .items(itemDTOs)
                .subtotal(subtotal)
                .build();
    }
}
