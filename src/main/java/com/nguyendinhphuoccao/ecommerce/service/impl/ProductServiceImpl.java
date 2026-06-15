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
    private final com.nguyendinhphuoccao.ecommerce.repository.CategoryRepository categoryRepository;
    private final com.nguyendinhphuoccao.ecommerce.repository.VariantOptionRepository variantOptionRepository;
    private final com.nguyendinhphuoccao.ecommerce.repository.CustomerFavoriteRepository customerFavoriteRepository;
    private final com.nguyendinhphuoccao.ecommerce.repository.CardItemRepository cardItemRepository;

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

    private void processCategory(ProductRequestDTO request, Product product) {
        if (request.getCategoryName() != null && !request.getCategoryName().trim().isEmpty()) {
            com.nguyendinhphuoccao.ecommerce.entity.Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                .orElseGet(() -> {
                    com.nguyendinhphuoccao.ecommerce.entity.Category newCat = new com.nguyendinhphuoccao.ecommerce.entity.Category();
                    newCat.setCategoryName(request.getCategoryName());
                    newCat.setCreatedBy(getCurrentStaff());
                    newCat.setCreatedAt(java.time.OffsetDateTime.now());
                    newCat.setUpdatedAt(java.time.OffsetDateTime.now());
                    return categoryRepository.save(newCat);
                });
            
            com.nguyendinhphuoccao.ecommerce.entity.ProductCategory pc = new com.nguyendinhphuoccao.ecommerce.entity.ProductCategory();
            pc.setCategory(category);
            pc.setProduct(product);

            if (product.getProductCategories() == null) {
                product.setProductCategories(new java.util.ArrayList<>());
            }
            product.getProductCategories().add(pc);
        }
    }

    private void processGalleries(ProductRequestDTO request, Product product) {
        if (request.getGalleries() != null && !request.getGalleries().isEmpty()) {
            List<com.nguyendinhphuoccao.ecommerce.entity.Gallery> galleries = new java.util.ArrayList<>();
            for (ProductRequestDTO.GalleryDTO dto : request.getGalleries()) {
                com.nguyendinhphuoccao.ecommerce.entity.Gallery gallery = new com.nguyendinhphuoccao.ecommerce.entity.Gallery();
                gallery.setImage(dto.getImageUrl());
                gallery.setIsThumbnail(dto.getIsThumbnail() != null ? dto.getIsThumbnail() : false);
                gallery.setPlaceholder("");
                gallery.setProduct(product);
                gallery.setCreatedAt(java.time.OffsetDateTime.now());
                gallery.setUpdatedAt(java.time.OffsetDateTime.now());
                galleries.add(gallery);
            }
            if (product.getGalleries() != null) {
                product.getGalleries().clear();
                product.getGalleries().addAll(galleries);
            } else {
                product.setGalleries(galleries);
            }
        }
    }

    private void ensureRequiredFields(Product product) {
        if (product.getSlug() == null || product.getSlug().isEmpty()) {
            product.setSlug(java.util.UUID.randomUUID().toString());
        }
        if (product.getQuantity() == null) {
            product.setQuantity(100); // default
        }
        if (product.getShortDescription() == null) {
            product.setShortDescription(product.getProductName() != null ? product.getProductName() : "No short description");
        }
        if (product.getProductDescription() == null) {
            product.setProductDescription(product.getProductName() != null ? product.getProductName() : "No description");
        }
        if (product.getPublished() == null) {
            product.setPublished(true); // default to published so it shows on home page
        }
    }

    @Override
    public Product create(ProductRequestDTO request) {
        Product product = new Product();
        product = mapDtoToProduct(request, product);
        product.setCreatedBy(getCurrentStaff());
        product.setUpdatedAt(java.time.OffsetDateTime.now());
        product.setCreatedAt(java.time.OffsetDateTime.now());
        
        ensureRequiredFields(product);
        processTags(request, product);
        processCategory(request, product);
        processGalleries(request, product);
        
        return repository.save(product);
    }

    @Override
    public Product update(UUID id, ProductRequestDTO request) {
        return repository.findById(id).map(product -> {
            product = mapDtoToProduct(request, product);
            product.setUpdatedBy(getCurrentStaff());
            product.setUpdatedAt(java.time.OffsetDateTime.now());
            
            ensureRequiredFields(product);
            processTags(request, product);
            processCategory(request, product);
            processGalleries(request, product);
            
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
        UUID customerId = getCurrentCustomerId();
        List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductHomeResponseDTO> results = repository.findProductsByTagName(tagName, customerId);
        if (!results.isEmpty()) {
            List<UUID> productIds = results.stream().map(com.nguyendinhphuoccao.ecommerce.dto.product.ProductHomeResponseDTO::getId).toList();
            List<Object[]> tagResults = tagRepository.findTagNamesByProductIds(productIds);
            java.util.Map<UUID, java.util.List<String>> tagMap = new java.util.HashMap<>();
            for (Object[] row : tagResults) {
                UUID pId = (UUID) row[0];
                String tName = (String) row[1];
                tagMap.computeIfAbsent(pId, k -> new java.util.ArrayList<>()).add(tName);
            }
            for (com.nguyendinhphuoccao.ecommerce.dto.product.ProductHomeResponseDTO dto : results) {
                dto.setTags(tagMap.getOrDefault(dto.getId(), new java.util.ArrayList<>()));
            }
        }
        return results;
    }

    private UUID getCurrentCustomerId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            com.nguyendinhphuoccao.ecommerce.entity.Customer customer = ((CustomUserDetails) auth.getPrincipal()).getCustomer();
            if (customer != null) {
                return customer.getId();
            }
        }
        // Return null instead of throwing an exception, so Staff or other accounts 
        // can still view the products (with isFavorite defaulting to false).
        return null;
    }

    private void populateTags(List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) return;
        List<UUID> productIds = dtos.stream().map(com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO::getId).toList();
        List<Object[]> tagResults = tagRepository.findTagNamesByProductIds(productIds);
        java.util.Map<UUID, java.util.List<String>> tagMap = new java.util.HashMap<>();
        for (Object[] row : tagResults) {
            UUID pId = (UUID) row[0];
            String tName = (String) row[1];
            tagMap.computeIfAbsent(pId, k -> new java.util.ArrayList<>()).add(tName);
        }
        for (com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO dto : dtos) {
            dto.setTags(tagMap.getOrDefault(dto.getId(), new java.util.ArrayList<>()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO> getProductsByCategory(UUID categoryId, String tagName) {
        UUID customerId = getCurrentCustomerId();
        
        if (categoryId.toString().equals("ffffffff-ffff-ffff-ffff-ffffffffffff")) {
            List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO> topsList = new java.util.ArrayList<>();
            List<com.nguyendinhphuoccao.ecommerce.entity.Category> allCategories = categoryRepository.findAll();
            for (com.nguyendinhphuoccao.ecommerce.entity.Category cat : allCategories) {
                if (cat.getActive() == null || cat.getActive()) {
                    List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO> prods;
                    if (tagName != null && !tagName.trim().isEmpty()) {
                        prods = repository.findProductsByCategoryIdAndTagAndCustomerId(cat.getId(), tagName, customerId);
                    } else {
                        prods = repository.findProductsByCategoryIdAndCustomerId(cat.getId(), customerId);
                    }
                    topsList.addAll(prods.stream().limit(2).toList());
                }
            }
            populateTags(topsList);
            return topsList;
        }

        com.nguyendinhphuoccao.ecommerce.entity.Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new com.nguyendinhphuoccao.ecommerce.exception.ResourceNotFoundException("Category not found with id: " + categoryId));
        if (category.getActive() != null && !category.getActive()) {
            throw new com.nguyendinhphuoccao.ecommerce.exception.ResourceNotFoundException("Category is inactive");
        }
        
        List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO> result;
        if (tagName != null && !tagName.trim().isEmpty()) {
            result = repository.findProductsByCategoryIdAndTagAndCustomerId(categoryId, tagName, customerId);
        } else {
            result = repository.findProductsByCategoryIdAndCustomerId(categoryId, customerId);
        }
        populateTags(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.nguyendinhphuoccao.ecommerce.dto.product.VariantOptionDTO> getVariantsByProductId(UUID productId) {
        return variantOptionRepository.findByProductId(productId).stream()
            .filter(v -> v.getActive() == null || v.getActive())
            .map(v -> com.nguyendinhphuoccao.ecommerce.dto.product.VariantOptionDTO.builder()
                .id(v.getId())
                .title(v.getTitle())
                .salePrice(v.getSalePrice())
                .comparePrice(v.getComparePrice())
                .quantity(v.getQuantity())
                .imageUrl(v.getImage() != null ? v.getImage().getImage() : null)
                .build())
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public com.nguyendinhphuoccao.ecommerce.dto.product.ProductDetailResponseDTO getProductDetails(UUID id) {
        Product product = repository.findById(id).orElseThrow(() -> new com.nguyendinhphuoccao.ecommerce.exception.ResourceNotFoundException("Product not found"));
        
        UUID customerId = getCurrentCustomerId();
        boolean isFavorite = false;
        int cartQuantity = 0;
        
        if (customerId != null) {
            isFavorite = customerFavoriteRepository.existsByCustomerIdAndProductId(customerId, id);
            Integer sum = cardItemRepository.sumQuantityByCustomerAndProduct(customerId, id);
            if (sum != null) {
                cartQuantity = sum;
            }
        }

        List<String> tags = new java.util.ArrayList<>();
        List<Object[]> tagResults = tagRepository.findTagNamesByProductIds(java.util.List.of(id));
        for (Object[] row : tagResults) {
            tags.add((String) row[1]);
        }

        double avgRating = 0.0;
        long totalReviews = 0;
        if (product.getProductReviews() != null && !product.getProductReviews().isEmpty()) {
            List<com.nguyendinhphuoccao.ecommerce.entity.ProductReview> pubReviews = product.getProductReviews().stream().filter(r -> r.getIsApproved() != null && r.getIsApproved()).toList();
            totalReviews = pubReviews.size();
            if (totalReviews > 0) {
                avgRating = pubReviews.stream().mapToDouble(com.nguyendinhphuoccao.ecommerce.entity.ProductReview::getRating).average().orElse(0.0);
            }
        }

        List<String> galleryUrls = new java.util.ArrayList<>();
        if (product.getGalleries() != null) {
            galleryUrls = product.getGalleries().stream()
                .map(com.nguyendinhphuoccao.ecommerce.entity.Gallery::getImage)
                .toList();
        }

        return com.nguyendinhphuoccao.ecommerce.dto.product.ProductDetailResponseDTO.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .sku(product.getSku())
                .salePrice(product.getSalePrice())
                .comparePrice(product.getComparePrice())
                .averageRating(avgRating)
                .totalReviews(totalReviews)
                .shortDescription(product.getShortDescription())
                .productDescription(product.getProductDescription())
                .isFavorite(isFavorite)
                .galleries(galleryUrls)
                .tags(tags)
                .cartQuantity(cartQuantity)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductHomeResponseDTO> getRelatedProducts(UUID productId) {
        Product product = repository.findById(productId).orElseThrow(() -> new com.nguyendinhphuoccao.ecommerce.exception.ResourceNotFoundException("Product not found"));
        UUID customerId = getCurrentCustomerId();
        
        if (product.getProductCategories() != null && !product.getProductCategories().isEmpty()) {
            UUID categoryId = product.getProductCategories().get(0).getCategory().getId();
            // Get products by category, which returns ProductCategoryResponseDTO
            List<com.nguyendinhphuoccao.ecommerce.dto.product.ProductCategoryResponseDTO> catProducts = repository.findProductsByCategoryIdAndCustomerId(categoryId, customerId);
            populateTags(catProducts);
            
            // Map to ProductHomeResponseDTO and exclude current product
            return catProducts.stream()
                .filter(p -> !p.getId().equals(productId))
                .limit(10) // Limit related products to 10
                .map(p -> com.nguyendinhphuoccao.ecommerce.dto.product.ProductHomeResponseDTO.builder()
                    .id(p.getId())
                    .productName(p.getProductName())
                    .sku(p.getSku())
                    .salePrice(p.getSalePrice())
                    .comparePrice(p.getComparePrice())
                    .thumbnailUrl(p.getThumbnailUrl())
                    .averageRating(p.getAverageRating())
                    .totalReviews(p.getTotalReviews())
                    .isFavorite(p.getIsFavorite())
                    .tags(p.getTags())
                    .build())
                .collect(java.util.stream.Collectors.toList());
        }
        return new java.util.ArrayList<>();
    }
}
