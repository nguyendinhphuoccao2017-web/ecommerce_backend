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
public class FavoriteResponseDTO {
    private UUID productId;
    private String productName;
    private String slug;
    private BigDecimal salePrice;
    private BigDecimal comparePrice;
    private String thumbnailUrl;
    private Double averageRating;
    private Long totalReviews;
    private Boolean isFavorite;
    private List<String> tags;
    private String variantTitle;
    private UUID variantOptionId;

    public FavoriteResponseDTO(UUID productId, String productName, String slug, BigDecimal salePrice, BigDecimal comparePrice, String thumbnailUrl, Double averageRating, Long totalReviews, Boolean isFavorite, String variantTitle, UUID variantOptionId) {
        this.productId = productId;
        this.productName = productName;
        this.slug = slug;
        this.salePrice = salePrice;
        this.comparePrice = comparePrice;
        this.thumbnailUrl = thumbnailUrl;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.isFavorite = isFavorite;
        this.variantTitle = variantTitle;
        this.variantOptionId = variantOptionId;
    }
}
