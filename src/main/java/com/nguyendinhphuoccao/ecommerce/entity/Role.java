package com.nguyendinhphuoccao.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @Column(name = "privileges", columnDefinition = "TEXT")
    private String privileges;

    @OneToMany(mappedBy = "role")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<StaffAccount> staffAccounts;
}
