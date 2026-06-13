package com.nguyendinhphuoccao.ecommerce.repository;

import com.nguyendinhphuoccao.ecommerce.entity.CardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CardItemRepository extends JpaRepository<CardItem, UUID> {
    
    @org.springframework.data.jpa.repository.Query("SELECT SUM(ci.quantity) FROM CardItem ci JOIN ci.card c WHERE c.customer.id = :customerId AND ci.product.id = :productId")
    Integer sumQuantityByCustomerAndProduct(@org.springframework.data.repository.query.Param("customerId") UUID customerId, @org.springframework.data.repository.query.Param("productId") UUID productId);
}
