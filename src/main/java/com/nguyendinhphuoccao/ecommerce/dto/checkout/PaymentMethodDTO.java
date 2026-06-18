package com.nguyendinhphuoccao.ecommerce.dto.checkout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {
    private UUID id;
    private String lastFourDigits;
    private String cardType;
    private String cardholderName;
    private Boolean isDefault;
}
