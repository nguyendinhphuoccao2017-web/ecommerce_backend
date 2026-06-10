package com.nguyendinhphuoccao.ecommerce.repository;

import com.nguyendinhphuoccao.ecommerce.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByTagName(String tagName);

    @org.springframework.data.jpa.repository.Query("SELECT pt.id, t.tagName FROM Tag t JOIN t.products pt WHERE pt.id IN :productIds")
    java.util.List<Object[]> findTagNamesByProductIds(@org.springframework.data.repository.query.Param("productIds") java.util.List<UUID> productIds);
}
