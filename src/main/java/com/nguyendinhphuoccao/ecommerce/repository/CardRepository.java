package com.nguyendinhphuoccao.ecommerce.repository;

import com.nguyendinhphuoccao.ecommerce.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
    Optional<Card> findByCustomerId(UUID customerId);
}
