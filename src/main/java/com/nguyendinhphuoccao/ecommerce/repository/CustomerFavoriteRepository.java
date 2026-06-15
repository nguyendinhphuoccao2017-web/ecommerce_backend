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

    @org.springframework.data.jpa.repository.Query("SELECT new com.nguyendinhphuoccao.ecommerce.dto.product.FavoriteResponseDTO(" +
            "p.id, p.productName, p.slug, COALESCE(vo.salePrice, p.salePrice), COALESCE(vo.comparePrice, p.comparePrice), " +
            "COALESCE(vo.image.image, (SELECT MAX(g.image) FROM Gallery g WHERE g.product.id = p.id AND g.isThumbnail = true)), " +
            "COALESCE(AVG(r.rating), 0.0), " +
            "COUNT(r.id), true, vo.title, vo.id, p.sku) " +
            "FROM CustomerFavorite cf " +
            "JOIN cf.product p " +
            "LEFT JOIN cf.variantOption vo " +
            "LEFT JOIN p.productReviews r ON r.isApproved = true " +
            "WHERE cf.customer.id = :customerId AND p.published = true " +
            "GROUP BY p.id, p.productName, p.slug, p.salePrice, p.comparePrice, vo.salePrice, vo.comparePrice, vo.image.image, vo.title, vo.id, cf.createdAt, p.sku " +
            "ORDER BY cf.createdAt DESC")
    java.util.List<com.nguyendinhphuoccao.ecommerce.dto.product.FavoriteResponseDTO> findFavoriteProductsWithDetails(@org.springframework.data.repository.query.Param("customerId") UUID customerId);
}
