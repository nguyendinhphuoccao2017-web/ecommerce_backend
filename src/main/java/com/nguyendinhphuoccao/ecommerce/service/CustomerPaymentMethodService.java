package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.dto.checkout.PaymentMethodDTO;
import java.util.List;
import java.util.UUID;

public interface CustomerPaymentMethodService {
    List<PaymentMethodDTO> getPaymentMethodsByCustomer(UUID customerId);
    PaymentMethodDTO getDefaultPaymentMethod(UUID customerId);
}
