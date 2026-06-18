package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.dto.checkout.PaymentMethodDTO;
import com.nguyendinhphuoccao.ecommerce.entity.CustomerPaymentMethod;
import com.nguyendinhphuoccao.ecommerce.repository.CustomerPaymentMethodRepository;
import com.nguyendinhphuoccao.ecommerce.service.CustomerPaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerPaymentMethodServiceImpl implements CustomerPaymentMethodService {

    private final CustomerPaymentMethodRepository repository;

    @Override
    public List<PaymentMethodDTO> getPaymentMethodsByCustomer(UUID customerId) {
        return repository.findByCustomerId(customerId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PaymentMethodDTO getDefaultPaymentMethod(UUID customerId) {
        List<CustomerPaymentMethod> methods = repository.findByCustomerId(customerId);
        if (methods.isEmpty()) return null;
        CustomerPaymentMethod defaultMethod = methods.stream()
                .filter(CustomerPaymentMethod::getIsDefault)
                .findFirst()
                .orElse(methods.get(0));
        return mapToDTO(defaultMethod);
    }

    private PaymentMethodDTO mapToDTO(CustomerPaymentMethod entity) {
        return PaymentMethodDTO.builder()
                .id(entity.getId())
                .lastFourDigits(entity.getLastFourDigits())
                .cardType(entity.getCardType())
                .cardholderName(entity.getCardholderName())
                .isDefault(entity.getIsDefault())
                .build();
    }
}
