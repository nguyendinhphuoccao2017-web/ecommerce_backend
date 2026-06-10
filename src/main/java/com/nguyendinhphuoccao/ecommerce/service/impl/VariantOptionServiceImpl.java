package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.entity.VariantOption;
import com.nguyendinhphuoccao.ecommerce.repository.VariantOptionRepository;
import com.nguyendinhphuoccao.ecommerce.repository.ProductRepository;
import com.nguyendinhphuoccao.ecommerce.entity.Product;
import com.nguyendinhphuoccao.ecommerce.entity.Gallery;
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
    private final com.nguyendinhphuoccao.ecommerce.repository.CustomerFavoriteRepository customerFavoriteRepository;

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
        // Xóa hết các variant cũ để gen lại cho chuẩn UI
        customerFavoriteRepository.deleteAll();
        repository.deleteAll();
        
        String[] sizes = {"XS", "S", "M", "L", "XL"};
        String[] colors = {"Color: Black", "Color: White", "Color: Red", "Color: Blue", "Color: Green"};
        
        for (Product product : products) {
            // Generate Sizes
            for (String size : sizes) {
                VariantOption varSize = VariantOption.builder()
                        .product(product)
                        .title(size)
                        .salePrice(product.getSalePrice() != null ? product.getSalePrice() : java.math.BigDecimal.valueOf(100))
                        .comparePrice(product.getComparePrice())
                        .quantity(100)
                        .active(true)
                        .build();
                repository.save(varSize);
            }
            
            // Generate Colors mapped to Gallery Images
            if (product.getGalleries() != null && !product.getGalleries().isEmpty()) {
                int colorIndex = 0;
                for (Gallery gallery : product.getGalleries()) {
                    String colorName = colors[colorIndex % colors.length];
                    VariantOption varColor = VariantOption.builder()
                            .product(product)
                            .title(colorName)
                            .image(gallery)
                            .salePrice(product.getSalePrice() != null ? product.getSalePrice() : java.math.BigDecimal.valueOf(100))
                            .comparePrice(product.getComparePrice())
                            .quantity(100)
                            .active(true)
                            .build();
                    repository.save(varColor);
                    colorIndex++;
                }
            }
        }
    }
}
