package com.nguyendinhphuoccao.ecommerce.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {
    private UUID id;
    private Integer rating;
    private String title;
    private String comment;
    private List<String> images;
    private Integer helpfulCount;
    private OffsetDateTime createdAt;
    private String firstName;
    private String lastName;
    private String avatar;

    public ReviewResponseDTO(UUID id, Integer rating, String title, String comment, List<String> images, 
                             Integer helpfulCount, OffsetDateTime createdAt, String firstName, String lastName) {
        this.id = id;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.images = images;
        this.helpfulCount = helpfulCount != null ? helpfulCount : 0;
        this.createdAt = createdAt;
        this.firstName = firstName != null ? firstName : "Anonymous";
        this.lastName = lastName != null ? lastName : "User";
        this.avatar = null;
    }
}
