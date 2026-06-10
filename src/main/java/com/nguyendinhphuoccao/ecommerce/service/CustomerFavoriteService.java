package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO;
import java.util.List;
import java.util.UUID;

public interface CustomerFavoriteService {
    List<com.nguyendinhphuoccao.ecommerce.dto.product.FavoriteResponseDTO> getFavoriteProducts();
    void toggleFavorite(UUID productId, com.nguyendinhphuoccao.ecommerce.dto.product.FavoriteToggleRequest request);
    void clearMyFavorites();
}
