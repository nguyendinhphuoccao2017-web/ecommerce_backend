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
        List<CustomerFavorite> favorites = customerFavoriteRepository.findByCustomerId(customer.getId());
        
        // Lọc trùng lặp (nếu có lỗi DB cũ) để đảm bảo mỗi sản phẩm chỉ xuất hiện 1 lần
        java.util.Set<UUID> seenProductIds = new java.util.HashSet<>();
        List<CustomerFavorite> uniqueFavorites = new java.util.ArrayList<>();
        for (CustomerFavorite fav : favorites) {
            if (seenProductIds.add(fav.getProduct().getId())) {
                uniqueFavorites.add(fav);
            }
        }
        
        List<com.nguyendinhphuoccao.ecommerce.dto.product.FavoriteResponseDTO> result = new java.util.ArrayList<>();
        for (CustomerFavorite fav : uniqueFavorites) {
            Product product = fav.getProduct();
            
            Double avgRating = 0.0;
            Long totalReviews = 0L;
            if (product.getProductReviews() != null && !product.getProductReviews().isEmpty()) {
                totalReviews = (long) product.getProductReviews().size();
                avgRating = product.getProductReviews().stream()
                        .mapToDouble(com.nguyendinhphuoccao.ecommerce.entity.ProductReview::getRating)
                        .average().orElse(0.0);
            }
            
            String thumbnailUrl = null;
            if (product.getGalleries() != null) {
                for (com.nguyendinhphuoccao.ecommerce.entity.Gallery g : product.getGalleries()) {
                    if (g.getIsThumbnail() != null && g.getIsThumbnail()) {
                        thumbnailUrl = g.getImage();
                        break;
                    }
                }
            }
            
            List<String> tagNames = new java.util.ArrayList<>();
            if (product.getTags() != null) {
                tagNames = product.getTags().stream().map(com.nguyendinhphuoccao.ecommerce.entity.Tag::getTagName).toList();
            }
            
            java.math.BigDecimal salePrice = product.getSalePrice();
            java.math.BigDecimal comparePrice = product.getComparePrice();
            String variantTitle = null;
            UUID variantOptionId = null;
            
            if (fav.getVariantOption() != null) {
                salePrice = fav.getVariantOption().getSalePrice();
                comparePrice = fav.getVariantOption().getComparePrice();
                variantTitle = fav.getVariantOption().getTitle();
                variantOptionId = fav.getVariantOption().getId();
                if (fav.getVariantOption().getImage() != null) {
                    thumbnailUrl = fav.getVariantOption().getImage().getImage();
                }
            }

            result.add(com.nguyendinhphuoccao.ecommerce.dto.product.FavoriteResponseDTO.builder()
                    .productId(product.getId())
                    .productName(product.getProductName())
                    .slug(product.getSlug())
                    .salePrice(salePrice)
                    .comparePrice(comparePrice)
                    .thumbnailUrl(thumbnailUrl)
                    .averageRating(avgRating)
                    .totalReviews(totalReviews)
                    .isFavorite(true)
                    .tags(tagNames)
                    .variantTitle(variantTitle)
                    .variantOptionId(variantOptionId)
                    .build());
        }
        return result;
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
