package com.nguyendinhphuoccao.ecommerce.repository;

import com.nguyendinhphuoccao.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    java.util.Optional<Category> findByCategoryName(String categoryName);

    @org.springframework.data.jpa.repository.Query("SELECT pc.product.id, c.categoryName FROM Category c JOIN c.productCategories pc WHERE pc.product.id IN :productIds")
    java.util.List<Object[]> findCategoryNamesByProductIds(@org.springframework.data.repository.query.Param("productIds") java.util.List<UUID> productIds);
}
