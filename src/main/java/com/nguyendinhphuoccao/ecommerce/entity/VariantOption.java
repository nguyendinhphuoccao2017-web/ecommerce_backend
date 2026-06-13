package com.nguyendinhphuoccao.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "variant_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantOption {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Gallery image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "sale_price", nullable = false)
    private BigDecimal salePrice;

    @Column(name = "compare_price")
    private BigDecimal comparePrice;

    @Column(name = "buying_price")
    private BigDecimal buyingPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "sku")
    private String sku;

    @Column(name = "active")
    private Boolean active;
}
