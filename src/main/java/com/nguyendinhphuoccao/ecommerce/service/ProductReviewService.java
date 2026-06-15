package com.nguyendinhphuoccao.ecommerce.service;

import com.nguyendinhphuoccao.ecommerce.dto.review.ReviewRequestDTO;
import com.nguyendinhphuoccao.ecommerce.dto.review.ReviewResponseDTO;
import com.nguyendinhphuoccao.ecommerce.dto.review.ReviewSummaryDTO;
import com.nguyendinhphuoccao.ecommerce.entity.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductReviewService {
    ProductReview create(ProductReview entity);
    ProductReview update(UUID id, ProductReview entity);
    void delete(UUID id);
    ProductReview getById(UUID id);
    List<ProductReview> getAll();

    Page<ReviewResponseDTO> getProductReviews(UUID productId, boolean withPhoto, Pageable pageable);
    ReviewSummaryDTO getProductReviewSummary(UUID productId);
    ProductReview addReview(UUID productId, ReviewRequestDTO requestDTO);
    void incrementHelpfulCount(UUID reviewId);
}
