package com.nguyendinhphuoccao.ecommerce.dto.product;

import lombok.Data;
import java.util.UUID;

@Data
public class FavoriteToggleRequest {
    private UUID variantOptionId;
}
