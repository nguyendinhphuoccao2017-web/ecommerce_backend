package com.nguyendinhphuoccao.ecommerce.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductHomeResponseDTO {
    private UUID id;
    private String productName;
    private BigDecimal salePrice;
    private BigDecimal comparePrice;
    private String thumbnailUrl;
    private Double averageRating;
    private Long totalReviews;
    private String placeholder;

    public ProductHomeResponseDTO(UUID id, String productName, BigDecimal salePrice, BigDecimal comparePrice, String thumbnailUrl, Double averageRating, Long totalReviews) {
        this.id = id;
        this.productName = productName;
        this.salePrice = salePrice;
        this.comparePrice = comparePrice;
        this.thumbnailUrl = thumbnailUrl;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.placeholder = null;
    }
}
