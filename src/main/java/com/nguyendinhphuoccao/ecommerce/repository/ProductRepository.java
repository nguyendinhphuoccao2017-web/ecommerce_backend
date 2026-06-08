package com.nguyendinhphuoccao.ecommerce.repository;

import com.nguyendinhphuoccao.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

import com.nguyendinhphuoccao.ecommerce.dto.product.ProductHomeResponseDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT new com.nguyendinhphuoccao.ecommerce.dto.product.ProductHomeResponseDTO(" +
            "p.id, p.productName, p.salePrice, p.comparePrice, " +
            "(SELECT MAX(g.image) FROM Gallery g WHERE g.product.id = p.id AND g.isThumbnail = true), " +
            "COALESCE(AVG(r.rating), 0.0), " +
            "COUNT(r.id), p.sku) " +
            "FROM Product p " +
            "JOIN p.tags t " +
            "LEFT JOIN p.productReviews r " +
            "WHERE t.tagName = :tagName AND p.published = true " +
            "GROUP BY p.id, p.productName, p.salePrice, p.comparePrice, p.sku " +
            "ORDER BY p.createdAt DESC")
    List<ProductHomeResponseDTO> findProductsByTagName(@Param("tagName") String tagName);
}
