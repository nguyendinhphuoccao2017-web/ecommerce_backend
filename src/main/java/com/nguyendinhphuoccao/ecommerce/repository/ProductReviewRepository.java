package com.nguyendinhphuoccao.ecommerce.repository;

import com.nguyendinhphuoccao.ecommerce.dto.review.ReviewResponseDTO;
import com.nguyendinhphuoccao.ecommerce.entity.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, UUID> {

    @Query("""
        SELECT new com.nguyendinhphuoccao.ecommerce.dto.review.ReviewResponseDTO(
            r.id, r.rating, r.title, r.comment, r.images, r.helpfulCount, r.createdAt,
            c.firstName, c.lastName
        )
        FROM ProductReview r
        LEFT JOIN r.customer c
        WHERE r.product.id = :productId
          AND r.isApproved = true
          AND (:withPhoto = false OR CAST(cardinality(r.images) AS Integer) > 0)
    """)
    Page<ReviewResponseDTO> findApprovedReviewsByProductId(
            @Param("productId") UUID productId,
            @Param("withPhoto") boolean withPhoto,
            Pageable pageable);

    @Query("SELECT COUNT(r) FROM ProductReview r WHERE r.product.id = :productId AND r.isApproved = true AND r.rating = :rating")
    Long countReviewsByProductIdAndRating(@Param("productId") UUID productId, @Param("rating") Integer rating);

    @Query("SELECT COUNT(r.id) FROM ProductReview r WHERE r.product.id = :productId AND r.isApproved = true")
    Long countTotalApprovedReviews(@Param("productId") UUID productId);

    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM ProductReview r WHERE r.product.id = :productId AND r.isApproved = true")
    Double getAverageRating(@Param("productId") UUID productId);

    @Modifying
    @Query("UPDATE ProductReview r SET r.helpfulCount = COALESCE(r.helpfulCount, 0) + 1 WHERE r.id = :reviewId")
    void incrementHelpfulCount(@Param("reviewId") UUID reviewId);

    boolean existsByProductIdAndCustomerId(UUID productId, UUID customerId);
}
