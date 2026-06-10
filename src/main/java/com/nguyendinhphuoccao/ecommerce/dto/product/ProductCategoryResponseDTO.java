package com.nguyendinhphuoccao.ecommerce.dto.product;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategoryResponseDTO {
    private UUID id;
    private String productName;
    private String slug;
    private BigDecimal salePrice;
    private BigDecimal comparePrice;
    private String thumbnailUrl;
    private Double averageRating;
    private Long totalReviews;
    private Boolean isFavorite;
    private List<String> tags;

    public ProductCategoryResponseDTO(UUID id, String productName, String slug, BigDecimal salePrice, BigDecimal comparePrice, String thumbnailUrl, Double averageRating, Long totalReviews, Boolean isFavorite) {
        this.id = id;
        this.productName = productName;
        this.slug = slug;
        this.salePrice = salePrice;
        this.comparePrice = comparePrice;
        this.thumbnailUrl = thumbnailUrl;
        this.averageRating = averageRating != null ? averageRating : 0.0;
        this.totalReviews = totalReviews != null ? totalReviews : 0L;
        this.isFavorite = isFavorite != null ? isFavorite : false;
        this.tags = new java.util.ArrayList<>();
    }
}
