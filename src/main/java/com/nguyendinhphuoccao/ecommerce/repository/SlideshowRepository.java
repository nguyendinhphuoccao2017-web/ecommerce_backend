package com.nguyendinhphuoccao.ecommerce.repository;

import com.nguyendinhphuoccao.ecommerce.entity.Slideshow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface SlideshowRepository extends JpaRepository<Slideshow, UUID> {
    List<Slideshow> findTop2ByPublishedTrueOrderByDisplayOrderAsc();
}
