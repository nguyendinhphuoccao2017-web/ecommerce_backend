package com.nguyendinhphuoccao.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "iso", nullable = false, length = 2)
    private String iso;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "upper_name", nullable = false, length = 80)
    private String upperName;

    @Column(name = "iso3", length = 3)
    private String iso3;

    @Column(name = "num_code")
    private Short numCode;

    @Column(name = "phone_code", nullable = false)
    private Integer phoneCode;
}
