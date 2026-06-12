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
            "COUNT(r.id), p.sku, " +
            "(CASE WHEN EXISTS (SELECT 1 FROM CustomerFavorite cf WHERE cf.product.id = p.id AND cf.customer.id = :customerId) THEN true ELSE false END)) " +
            "FROM Product p " +
            "JOIN p.tags t " +
            "LEFT JOIN p.productReviews r ON r.published = true " +
            "WHERE t.tagName = :tagName AND p.published = true " +
            "GROUP BY p.id, p.productName, p.salePrice, p.comparePrice, p.sku " +
            "ORDER BY p.createdAt DESC")
    List<ProductHomeResponseDTO> findProductsByTagName(@Param("tagName") String tagName, @Param("customerId") UUID customerId);

    @Query("SELECT new com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO(" +
            "p.id, p.productName, p.slug, p.salePrice, p.comparePrice, " +
            "(SELECT MAX(g.image) FROM Gallery g WHERE g.product.id = p.id AND g.isThumbnail = true), " +
            "COALESCE(AVG(r.rating), 0.0), " +
            "COUNT(r.id), " +
            "(CASE WHEN EXISTS (SELECT 1 FROM CustomerFavorite cf WHERE cf.product.id = p.id AND cf.customer.id = :customerId) THEN true ELSE false END), p.sku) " +
            "FROM Product p " +
            "JOIN p.productCategories pc " +
            "LEFT JOIN p.productReviews r ON r.published = true " +
            "WHERE pc.category.id = :categoryId AND p.published = true " +
            "GROUP BY p.id, p.productName, p.slug, p.salePrice, p.comparePrice, p.sku " +
            "ORDER BY p.createdAt DESC")
    List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO> findProductsByCategoryIdAndCustomerId(@Param("categoryId") UUID categoryId, @Param("customerId") UUID customerId);

    @Query("SELECT new com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO(" +
            "p.id, p.productName, p.slug, p.salePrice, p.comparePrice, " +
            "(SELECT MAX(g.image) FROM Gallery g WHERE g.product.id = p.id AND g.isThumbnail = true), " +
            "COALESCE(AVG(r.rating), 0.0), " +
            "COUNT(r.id), " +
            "(CASE WHEN EXISTS (SELECT 1 FROM CustomerFavorite cf WHERE cf.product.id = p.id AND cf.customer.id = :customerId) THEN true ELSE false END), p.sku) " +
            "FROM Product p " +
            "JOIN p.productCategories pc " +
            "JOIN p.tags t " +
            "LEFT JOIN p.productReviews r ON r.published = true " +
            "WHERE pc.category.id = :categoryId AND t.tagName = :tagName AND p.published = true " +
            "GROUP BY p.id, p.productName, p.slug, p.salePrice, p.comparePrice, p.sku " +
            "ORDER BY p.createdAt DESC")
    List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO> findProductsByCategoryIdAndTagAndCustomerId(@Param("categoryId") UUID categoryId, @Param("tagName") String tagName, @Param("customerId") UUID customerId);

    @Query("SELECT new com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO(" +
            "p.id, p.productName, p.slug, p.salePrice, p.comparePrice, " +
            "(SELECT MAX(g.image) FROM Gallery g WHERE g.product.id = p.id AND g.isThumbnail = true), " +
            "COALESCE(AVG(r.rating), 0.0), " +
            "COUNT(r.id), " +
            "true, p.sku) " +
            "FROM Product p " +
            "JOIN CustomerFavorite cf ON cf.product.id = p.id " +
            "LEFT JOIN p.productReviews r ON r.published = true " +
            "WHERE cf.customer.id = :customerId AND p.published = true " +
            "GROUP BY p.id, p.productName, p.slug, p.salePrice, p.comparePrice, cf.createdAt, p.sku " +
            "ORDER BY cf.createdAt DESC")
    List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO> findFavoriteProductsByCustomerId(@Param("customerId") UUID customerId);
}
