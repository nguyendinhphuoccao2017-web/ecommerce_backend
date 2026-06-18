package com.nguyendinhphuoccao.ecommerce.dto.cart;

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
public class CartItemDTO {
    private UUID id; // CardItem ID
    private UUID productId;
    private String productName;
    private String sku;
    private BigDecimal salePrice;
    private BigDecimal comparePrice;
    private String image;
    
    // Variant info
    private UUID variantOptionId;
    private String variantTitle; // e.g. "Cream, L"
    private String color;
    private String size;
    
    private Integer quantity;
    private Integer maxQuantity; // Available stock
}
