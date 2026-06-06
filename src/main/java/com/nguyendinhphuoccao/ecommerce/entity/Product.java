package com.nguyendinhphuoccao.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "slug", nullable = false, unique = true, columnDefinition = "TEXT")
    private String slug;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "sku")
    private String sku;

    @Column(name = "sale_price", nullable = false)
    private BigDecimal salePrice;

    @Column(name = "compare_price")
    private BigDecimal comparePrice;

    @Column(name = "buying_price")
    private BigDecimal buyingPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "short_description", nullable = false, length = 165)
    private String shortDescription;

    @Column(name = "product_description", nullable = false, columnDefinition = "TEXT")
    private String productDescription;

    @Column(name = "product_type", length = 64)
    private String productType;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "disable_out_of_stock")
    private Boolean disableOutOfStock;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private StaffAccount createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private StaffAccount updatedBy;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private ProductShippingInfo productShippingInfo;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Gallery> galleries;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<ProductCategory> productCategories;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<ProductTag> productTags;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<ProductSupplier> productSuppliers;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<ProductCoupon> productCoupons;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<ProductAttribute> productAttributes;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<VariantOption> variantOptions;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Variant> variants;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<ProductReview> productReviews;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Sell sell;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<OrderItem> orderItems;
}
