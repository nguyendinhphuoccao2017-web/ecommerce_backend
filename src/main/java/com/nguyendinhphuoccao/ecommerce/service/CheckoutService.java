package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.dto.cart.CheckoutRequestDTO;
import java.util.UUID;

public interface CheckoutService {
    void submitOrder(UUID customerId, CheckoutRequestDTO request);
}
