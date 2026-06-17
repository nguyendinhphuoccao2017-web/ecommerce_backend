package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.dto.cart.CheckoutRequestDTO;
import com.nguyendinhphuoccao.ecommerce.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/submit")
    public ResponseEntity<Void> submitOrder(
            @RequestBody CheckoutRequestDTO request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.nguyendinhphuoccao.ecommerce.security.CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getCustomer() == null || userDetails.getCustomer().getId() == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        
        checkoutService.submitOrder(userDetails.getCustomer().getId(), request);
        return ResponseEntity.ok().build();
    }
}
