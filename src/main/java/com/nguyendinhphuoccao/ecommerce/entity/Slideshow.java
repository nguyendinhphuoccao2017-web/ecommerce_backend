package com.nguyendinhphuoccao.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "slideshows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slideshow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", length = 80)
    private String title;

    @Column(name = "destination_url", columnDefinition = "TEXT")
    private String destinationUrl;

    @Column(name = "image", nullable = false, columnDefinition = "TEXT")
    private String image;

    @Column(name = "placeholder", nullable = false, columnDefinition = "TEXT")
    private String placeholder;

    @Column(name = "description", length = 160)
    private String description;

    @Column(name = "bm_label", length = 50)
    private String bmLabel;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "clicks", nullable = false)
    private Integer clicks;

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(name = "styles", columnDefinition = "JSONB")
    private String styles;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private StaffAccount createdBy;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private StaffAccount updatedBy;
}
