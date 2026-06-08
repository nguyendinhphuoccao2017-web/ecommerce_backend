package com.nguyendinhphuoccao.ecommerce.service.impl;

import com.nguyendinhphuoccao.ecommerce.dto.product.ProductRequestDTO;
import com.nguyendinhphuoccao.ecommerce.entity.Product;
import com.nguyendinhphuoccao.ecommerce.entity.StaffAccount;
import com.nguyendinhphuoccao.ecommerce.entity.Tag;
import com.nguyendinhphuoccao.ecommerce.repository.ProductRepository;
import com.nguyendinhphuoccao.ecommerce.repository.TagRepository;
import com.nguyendinhphuoccao.ecommerce.security.CustomUserDetails;
import com.nguyendinhphuoccao.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final TagRepository tagRepository;

    private StaffAccount getCurrentStaff() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            StaffAccount staff = ((CustomUserDetails) auth.getPrincipal()).getStaffAccount();
            if (staff != null) {
                return staff;
            }
        }
        throw new org.springframework.security.access.AccessDeniedException("Unauthorized: Only Staff Account can perform this action.");
    }

    private Product mapDtoToProduct(ProductRequestDTO dto, Product product) {
        if (dto.getProductName() != null) product.setProductName(dto.getProductName());
        if (dto.getSlug() != null) product.setSlug(dto.getSlug());
        if (dto.getSku() != null) product.setSku(dto.getSku());
        if (dto.getSalePrice() != null) product.setSalePrice(dto.getSalePrice());
        if (dto.getComparePrice() != null) product.setComparePrice(dto.getComparePrice());
        if (dto.getBuyingPrice() != null) product.setBuyingPrice(dto.getBuyingPrice());
        if (dto.getQuantity() != null) product.setQuantity(dto.getQuantity());
        if (dto.getShortDescription() != null) product.setShortDescription(dto.getShortDescription());
        if (dto.getProductDescription() != null) product.setProductDescription(dto.getProductDescription());
        if (dto.getProductType() != null) product.setProductType(dto.getProductType());
        if (dto.getPublished() != null) product.setPublished(dto.getPublished());
        return product;
    }

    private void processTags(ProductRequestDTO request, Product product) {
        if (request.getTags() != null) {
            List<Tag> tags = new java.util.ArrayList<>();
            for (String tagName : request.getTags()) {
                Tag tag = tagRepository.findByTagName(tagName).orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setTagName(tagName);
                    newTag.setCreatedBy(getCurrentStaff());
                    newTag.setUpdatedAt(java.time.OffsetDateTime.now());
                    newTag.setCreatedAt(java.time.OffsetDateTime.now());
                    return tagRepository.save(newTag);
                });
                tags.add(tag);
            }
            product.setTags(tags);
        }
    }

    @Override
    public Product create(ProductRequestDTO request) {
        Product product = new Product();
        product = mapDtoToProduct(request, product);
        product.setCreatedBy(getCurrentStaff());
        product.setUpdatedAt(java.time.OffsetDateTime.now());
        product.setCreatedAt(java.time.OffsetDateTime.now());
        
        processTags(request, product);
        
        return repository.save(product);
    }

    @Override
    public Product update(UUID id, ProductRequestDTO request) {
        return repository.findById(id).map(product -> {
            product = mapDtoToProduct(request, product);
            product.setUpdatedBy(getCurrentStaff());
            product.setUpdatedAt(java.time.OffsetDateTime.now());
            
            processTags(request, product);
            
            return repository.save(product);
        }).orElse(null);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductHomeResponseDTO> getProductsByTag(String tagName) {
        return repository.findProductsByTagName(tagName);
    }
}
