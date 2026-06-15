package com.nguyendinhphuoccao.ecommerce.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {
    private Integer rating;
    private String title;
    private String comment;
    private List<String> images;
}
