package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.dto.checkout.CheckoutInitDataDTO;
import java.util.UUID;

public interface CheckoutInitService {
    CheckoutInitDataDTO getInitData(UUID customerId);
}
