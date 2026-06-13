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
public class ProductDetailResponseDTO {
    private UUID id;
    private String productName;
    private String sku;
    private BigDecimal salePrice;
    private BigDecimal comparePrice;
    private Double averageRating;
    private Long totalReviews;
    private String shortDescription;
    private String productDescription;
    private Boolean isFavorite;
    private List<String> galleries;
    private List<String> tags;
    private Integer cartQuantity;
}
