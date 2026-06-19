package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.dto.checkout.CheckoutInitDataDTO;
import com.nguyendinhphuoccao.ecommerce.service.CheckoutInitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutInitController {

    private final CheckoutInitService checkoutInitService;

    @GetMapping("/init-data")
    public ResponseEntity<CheckoutInitDataDTO> getInitData(
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.nguyendinhphuoccao.ecommerce.security.CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getCustomer() == null || userDetails.getCustomer().getId() == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(checkoutInitService.getInitData(userDetails.getCustomer().getId()));
    }
}
