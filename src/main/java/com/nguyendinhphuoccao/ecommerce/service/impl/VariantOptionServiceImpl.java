package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.VariantOption;
import com.nguyendinhphuoccao.ecommerce.repository.VariantOptionRepository;
import com.nguyendinhphuoccao.ecommerce.repository.ProductRepository;
import com.nguyendinhphuoccao.ecommerce.entity.Product;
import com.nguyendinhphuoccao.ecommerce.service.VariantOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VariantOptionServiceImpl implements VariantOptionService {

    private final VariantOptionRepository repository;
    private final ProductRepository productRepository;

    @Override
    public VariantOption create(VariantOption entity) {
        return repository.save(entity);
    }

    @Override
    public VariantOption update(UUID id, VariantOption entity) {
        if(repository.existsById(id)) {
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
    public VariantOption getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VariantOption> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void generateDummyVariants() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            List<VariantOption> existing = repository.findByProductId(product.getId());
            if (existing == null || existing.isEmpty()) {
                VariantOption var1 = VariantOption.builder()
                        .product(product)
                        .title("Size: M, Color: Black")
                        .salePrice(product.getSalePrice() != null ? product.getSalePrice() : java.math.BigDecimal.valueOf(100))
                        .quantity(100)
                        .active(true)
                        .build();
                VariantOption var2 = VariantOption.builder()
                        .product(product)
                        .title("Size: L, Color: White")
                        .salePrice(product.getSalePrice() != null ? product.getSalePrice() : java.math.BigDecimal.valueOf(100))
                        .quantity(100)
                        .active(true)
                        .build();
                repository.save(var1);
                repository.save(var2);
            }
        }
    }
}
