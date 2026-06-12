package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO;
import com.nguyendinhphuoccao.ecommerce.entity.Customer;
import com.nguyendinhphuoccao.ecommerce.entity.CustomerFavorite;
import com.nguyendinhphuoccao.ecommerce.entity.Product;
import com.nguyendinhphuoccao.ecommerce.exception.ResourceNotFoundException;
import com.nguyendinhphuoccao.ecommerce.repository.CustomerFavoriteRepository;
import com.nguyendinhphuoccao.ecommerce.repository.ProductRepository;
import com.nguyendinhphuoccao.ecommerce.security.CustomUserDetails;
import com.nguyendinhphuoccao.ecommerce.service.CustomerFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerFavoriteServiceImpl implements CustomerFavoriteService {

    private final CustomerFavoriteRepository customerFavoriteRepository;
    private final ProductRepository productRepository;
    private final com.nguyendinhphuoccao.ecommerce.repository.VariantOptionRepository variantOptionRepository;
    private final com.nguyendinhphuoccao.ecommerce.repository.TagRepository tagRepository;

    private Customer getCurrentCustomer() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            Customer customer = ((CustomUserDetails) auth.getPrincipal()).getCustomer();
            if (customer != null) {
                return customer;
            }
        }
        throw new AccessDeniedException("Unauthorized: Only Customer Account can perform this action.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.nguyendinhphuoccao.ecommerce.dto.product.FavoriteResponseDTO> getFavoriteProducts() {
        Customer customer = getCurrentCustomer();
        List<com.nguyendinhphuoccao.ecommerce.dto.product.FavoriteResponseDTO> favorites = customerFavoriteRepository.findFavoriteProductsWithDetails(customer.getId());

        if (favorites.isEmpty()) {
            return favorites;
        }

        List<UUID> productIds = favorites.stream()
                .map(com.nguyendinhphuoccao.ecommerce.dto.product.FavoriteResponseDTO::getProductId)
                .toList();

        List<Object[]> tagsResult = tagRepository.findTagNamesByProductIds(productIds);
        java.util.Map<UUID, List<String>> productTagsMap = new java.util.HashMap<>();
        for (Object[] row : tagsResult) {
            UUID pId = (UUID) row[0];
            String tagName = (String) row[1];
            productTagsMap.computeIfAbsent(pId, k -> new java.util.ArrayList<>()).add(tagName);
        }

        for (com.nguyendinhphuoccao.ecommerce.dto.product.FavoriteResponseDTO dto : favorites) {
            dto.setTags(productTagsMap.getOrDefault(dto.getProductId(), new java.util.ArrayList<>()));
        }

        return favorites;
    }

    @Override
    @Transactional
    public void toggleFavorite(UUID productId, com.nguyendinhphuoccao.ecommerce.dto.product.FavoriteToggleRequest request) {
        Customer customer = getCurrentCustomer();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<CustomerFavorite> existingFavorites = customerFavoriteRepository.findByCustomerIdAndProductId(customer.getId(), productId);
        
        if (existingFavorites != null && !existingFavorites.isEmpty()) {
            // Nếu đã tồn tại (dù 1 hay nhiều do lỗi data cũ), xóa sạch để đảm bảo không trùng lặp
            customerFavoriteRepository.deleteAll(existingFavorites);
        } else {
            CustomerFavorite newFavorite = new CustomerFavorite();
            newFavorite.setCustomer(customer);
            newFavorite.setProduct(product);
            
            if (request != null && request.getVariantOptionId() != null) {
                com.nguyendinhphuoccao.ecommerce.entity.VariantOption variantOption = variantOptionRepository.findById(request.getVariantOptionId()).orElse(null);
                newFavorite.setVariantOption(variantOption);
            }
            
            customerFavoriteRepository.save(newFavorite);
        }
    }

    @Override
    @Transactional
    public void clearMyFavorites() {
        Customer customer = getCurrentCustomer();
        List<CustomerFavorite> favorites = customerFavoriteRepository.findByCustomerId(customer.getId());
        customerFavoriteRepository.deleteAll(favorites);
    }
}
