package com.nguyendinhphuoccao.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "customer_payment_methods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "last_four_digits", length = 4, nullable = false)
    private String lastFourDigits;

    @Column(name = "card_type", length = 20, nullable = false)
    private String cardType;

    @Column(name = "cardholder_name", nullable = false)
    private String cardholderName;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;
}
