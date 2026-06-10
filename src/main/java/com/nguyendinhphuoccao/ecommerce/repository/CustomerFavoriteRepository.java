package com.nguyendinhphuoccao.ecommerce.repository;

import com.nguyendinhphuoccao.ecommerce.entity.CustomerFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerFavoriteRepository extends JpaRepository<CustomerFavorite, UUID> {
    java.util.List<CustomerFavorite> findByCustomerIdAndProductId(UUID customerId, UUID productId);
    boolean existsByCustomerIdAndProductId(UUID customerId, UUID productId);
    java.util.List<CustomerFavorite> findByCustomerId(UUID customerId);
}
