package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.dto.checkout.CheckoutInitDataDTO;
import com.nguyendinhphuoccao.ecommerce.entity.CustomerAddress;
import com.nguyendinhphuoccao.ecommerce.service.CardService;
import com.nguyendinhphuoccao.ecommerce.service.CheckoutInitService;
import com.nguyendinhphuoccao.ecommerce.service.CustomerAddressService;
import com.nguyendinhphuoccao.ecommerce.service.CustomerPaymentMethodService;
import com.nguyendinhphuoccao.ecommerce.service.ShippingRateService;
import com.nguyendinhphuoccao.ecommerce.service.ShippingZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutInitServiceImpl implements CheckoutInitService {

    private final CustomerAddressService addressService;
    private final CustomerPaymentMethodService paymentMethodService;
    private final ShippingZoneService shippingZoneService;
    private final ShippingRateService shippingRateService;
    private final CardService cardService;

    @Override
    public CheckoutInitDataDTO getInitData(UUID customerId) {
        List<CustomerAddress> addresses = addressService.getMyAddresses(customerId);
        CustomerAddress defaultAddress = null;
        if (!addresses.isEmpty()) {
            defaultAddress = addresses.stream().filter(CustomerAddress::getIsActive).findFirst().orElse(addresses.get(0));
        }

        return CheckoutInitDataDTO.builder()
                .defaultAddress(defaultAddress)
                .defaultPaymentMethod(paymentMethodService.getDefaultPaymentMethod(customerId))
                .shippingZones(shippingZoneService.getAll())
                .shippingRates(shippingRateService.getAll())
                .cart(cardService.getCart(customerId))
                .build();
    }
}
