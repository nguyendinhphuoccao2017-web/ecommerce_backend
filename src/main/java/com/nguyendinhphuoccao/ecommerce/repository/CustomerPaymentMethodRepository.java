package com.nguyendinhphuoccao.ecommerce.repository;

import com.nguyendinhphuoccao.ecommerce.entity.CustomerPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerPaymentMethodRepository extends JpaRepository<CustomerPaymentMethod, UUID> {
    List<CustomerPaymentMethod> findByCustomerId(UUID customerId);
}
