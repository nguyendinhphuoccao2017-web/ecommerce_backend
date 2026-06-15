package com.nguyendinhphuoccao.ecommerce.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSummaryDTO {
    private Double averageRating;
    private Long totalReviews;
    private Map<Integer, Long> ratingDistribution;
}
