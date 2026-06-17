package com.nguyendinhphuoccao.ecommerce.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequestDTO {
    private UUID shippingAddressId;
    private String paymentMethod;
    private String deliveryMethod;
    private String couponCode;
}
