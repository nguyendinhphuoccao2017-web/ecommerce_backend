package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO;
import java.util.List;
import java.util.UUID;

public interface CustomerFavoriteService {
    List<ProductCategoryResponseDTO> getFavoriteProducts();
    void toggleFavorite(UUID productId);
}
