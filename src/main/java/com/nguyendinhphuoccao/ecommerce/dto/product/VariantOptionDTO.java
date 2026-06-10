package com.nguyendinhphuoccao.ecommerce.dto.product;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantOptionDTO {
    private UUID id;
    private String title;
    private BigDecimal salePrice;
    private BigDecimal comparePrice;
    private Integer quantity;
    private String imageUrl;
}
