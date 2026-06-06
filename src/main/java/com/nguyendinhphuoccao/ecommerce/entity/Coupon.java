package com.nguyendinhphuoccao.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "discount_value")
    private BigDecimal discountValue;

    @Column(name = "discount_type", nullable = false, length = 50)
    private String discountType;

    @Column(name = "times_used", nullable = false)
    private BigDecimal timesUsed;

    @Column(name = "max_usage")
    private BigDecimal maxUsage;

    @Column(name = "order_amount_limit")
    private BigDecimal orderAmountLimit;

    @Column(name = "coupon_start_date")
    private OffsetDateTime couponStartDate;

    @Column(name = "coupon_end_date")
    private OffsetDateTime couponEndDate;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<ProductCoupon> productCoupons;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Order> orders;
}
