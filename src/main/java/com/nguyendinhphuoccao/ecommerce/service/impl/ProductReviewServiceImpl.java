package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.dto.review.ReviewRequestDTO;
import com.nguyendinhphuoccao.ecommerce.dto.review.ReviewResponseDTO;
import com.nguyendinhphuoccao.ecommerce.dto.review.ReviewSummaryDTO;
import com.nguyendinhphuoccao.ecommerce.entity.Customer;
import com.nguyendinhphuoccao.ecommerce.entity.Product;
import com.nguyendinhphuoccao.ecommerce.entity.ProductReview;
import com.nguyendinhphuoccao.ecommerce.repository.ProductRepository;
import com.nguyendinhphuoccao.ecommerce.repository.ProductReviewRepository;
import com.nguyendinhphuoccao.ecommerce.security.CustomUserDetails;
import com.nguyendinhphuoccao.ecommerce.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository repository;
    private final ProductRepository productRepository;

    @Override
    public ProductReview create(ProductReview entity) {
        return repository.save(entity);
    }

    @Override
    public ProductReview update(UUID id, ProductReview entity) {
        if (repository.existsById(id)) {
            entity.setId(id);
            return repository.save(entity);
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductReview getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReview> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponseDTO> getProductReviews(UUID productId, boolean withPhoto, Pageable pageable) {
        return repository.findApprovedReviewsByProductId(productId, withPhoto, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewSummaryDTO getProductReviewSummary(UUID productId) {
        Double avgRating = repository.getAverageRating(productId);
        Long totalReviews = repository.countTotalApprovedReviews(productId);

        Map<Integer, Long> distribution = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            distribution.put(i, repository.countReviewsByProductIdAndRating(productId, i));
        }

        return new ReviewSummaryDTO(avgRating, totalReviews, distribution);
    }

    @Override
    public ProductReview addReview(UUID productId, ReviewRequestDTO requestDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof CustomUserDetails userDetails)) {
            throw new RuntimeException("User not authenticated");
        }

        Customer customer = userDetails.getCustomer();
        if (customer == null) {
            throw new RuntimeException("Only customers can leave reviews");
        }

        if (repository.existsByProductIdAndCustomerId(productId, customer.getId())) {
            throw new RuntimeException("You have already reviewed this product.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductReview review = ProductReview.builder()
                .product(product)
                .customer(customer)
                .rating(requestDTO.getRating())
                .title(requestDTO.getTitle())
                .comment(requestDTO.getComment())
                .images(requestDTO.getImages())
                .helpfulCount(0)
                .isApproved(true)
                .isVerifiedPurchase(false)
                .build();

        return repository.save(review);
    }

    @Override
    public void incrementHelpfulCount(UUID reviewId) {
        repository.incrementHelpfulCount(reviewId);
    }
}
