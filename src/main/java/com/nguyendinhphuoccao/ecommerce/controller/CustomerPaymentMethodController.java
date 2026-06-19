package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.dto.checkout.PaymentMethodDTO;
import com.nguyendinhphuoccao.ecommerce.service.CustomerPaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-methods")
@RequiredArgsConstructor
public class CustomerPaymentMethodController {

    private final CustomerPaymentMethodService service;

    @GetMapping("/my-methods")
    public ResponseEntity<List<PaymentMethodDTO>> getMyPaymentMethods(
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.nguyendinhphuoccao.ecommerce.security.CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getCustomer() == null || userDetails.getCustomer().getId() == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(service.getPaymentMethodsByCustomer(userDetails.getCustomer().getId()));
    }
}
