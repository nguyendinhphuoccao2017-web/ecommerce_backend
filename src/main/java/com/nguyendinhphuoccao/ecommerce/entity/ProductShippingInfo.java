package com.nguyendinhphuoccao.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product_shipping_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductShippingInfo {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "weight", nullable = false)
    private BigDecimal weight;

    @Column(name = "weight_unit", length = 10)
    private String weightUnit;

    @Column(name = "volume", nullable = false)
    private BigDecimal volume;

    @Column(name = "volume_unit", length = 10)
    private String volumeUnit;

    @Column(name = "dimension_width", nullable = false)
    private BigDecimal dimensionWidth;

    @Column(name = "dimension_height", nullable = false)
    private BigDecimal dimensionHeight;

    @Column(name = "dimension_depth", nullable = false)
    private BigDecimal dimensionDepth;

    @Column(name = "dimension_unit", length = 10)
    private String dimensionUnit;
}
