package com.nguyendinhphuoccao.ecommerce.dto.checkout;

import com.nguyendinhphuoccao.ecommerce.dto.cart.CartResponseDTO;
import com.nguyendinhphuoccao.ecommerce.entity.CustomerAddress;
import com.nguyendinhphuoccao.ecommerce.entity.ShippingRate;
import com.nguyendinhphuoccao.ecommerce.entity.ShippingZone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutInitDataDTO {
    private CustomerAddress defaultAddress;
    private PaymentMethodDTO defaultPaymentMethod;
    private List<ShippingZone> shippingZones;
    private List<ShippingRate> shippingRates;
    private CartResponseDTO cart;
}
