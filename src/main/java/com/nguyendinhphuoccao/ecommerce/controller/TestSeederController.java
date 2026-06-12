package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.entity.*;
import com.nguyendinhphuoccao.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestSeederController {

    private final CustomerFavoriteRepository customerFavoriteRepository;
    private final ProductRepository productRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final VariantOptionRepository variantOptionRepository;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;
    @PostMapping("/seed-colors")
    @Transactional
    public ResponseEntity<String> seedColorsAndCleanFavorites() {
        // 1. Delete all existing favorites to prevent foreign key errors
        customerFavoriteRepository.deleteAll();

        // 2. Clear old variants without images to keep it neat
        jdbcTemplate.execute("DELETE FROM variants WHERE variant_option_id IN (SELECT id FROM variant_options WHERE image_id IS NULL)");
        jdbcTemplate.execute("DELETE FROM variant_options WHERE image_id IS NULL");

        // 3. Clear legacy "Color: " variants that had images but are inaccurate
        jdbcTemplate.execute("DELETE FROM variants WHERE variant_option_id IN (SELECT id FROM variant_options WHERE title LIKE 'Color:%')");
        jdbcTemplate.execute("DELETE FROM variant_options WHERE title LIKE 'Color:%'");

        // For simplicity of this script, we just ensure Color attribute exists.
        
        Attribute colorAttr = attributeRepository.findAll().stream()
                .filter(a -> a.getAttributeName().equalsIgnoreCase("Color"))
                .findFirst()
                .orElseGet(() -> {
                    Attribute a = new Attribute();
                    a.setAttributeName("Color");
                    a.setCreatedAt(OffsetDateTime.now());
                    a.setUpdatedAt(OffsetDateTime.now());
                    return attributeRepository.save(a);
                });

        Attribute sizeAttr = attributeRepository.findAll().stream()
                .filter(a -> a.getAttributeName().equalsIgnoreCase("Size"))
                .findFirst()
                .orElseGet(() -> {
                    Attribute a = new Attribute();
                    a.setAttributeName("Size");
                    a.setCreatedAt(OffsetDateTime.now());
                    a.setUpdatedAt(OffsetDateTime.now());
                    return attributeRepository.save(a);
                });

        List<String> sizeNames = java.util.Arrays.asList("XS", "S", "M", "L", "XL");
        List<AttributeValue> sizeValues = new java.util.ArrayList<>();
        for (String sName : sizeNames) {
            AttributeValue sizeVal = attributeValueRepository.findAll().stream()
                    .filter(v -> v.getAttribute().getId().equals(sizeAttr.getId()) && v.getAttributeValue().equalsIgnoreCase(sName))
                    .findFirst()
                    .orElseGet(() -> {
                        AttributeValue v = new AttributeValue();
                        v.setAttribute(sizeAttr);
                        v.setAttributeValue(sName);
                        return attributeValueRepository.save(v);
                    });
            sizeValues.add(sizeVal);
        }

        List<Product> products = productRepository.findAll();

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            
            String colorStr = getColorForProduct(p.getProductName());
            AttributeValue assignedColor = getOrCreateColorValue(colorAttr, colorStr);

            // Link Product to Attributes if not already
            ProductAttribute paColor = productAttributeRepository.findAll().stream()
                    .filter(pa -> pa.getProduct().getId().equals(p.getId()) && pa.getAttribute().getId().equals(colorAttr.getId()))
                    .findFirst()
                    .orElseGet(() -> {
                        ProductAttribute pa = new ProductAttribute();
                        pa.setProduct(p);
                        pa.setAttribute(colorAttr);
                        return productAttributeRepository.save(pa);
                    });

            Gallery productGallery = null;
            if (p.getGalleries() != null && !p.getGalleries().isEmpty()) {
                productGallery = p.getGalleries().stream().filter(g -> Boolean.TRUE.equals(g.getIsThumbnail())).findFirst().orElse(p.getGalleries().get(0));
            }
            final Gallery finalGallery = productGallery;

            for (AttributeValue sizeVal : sizeValues) {
                ProductAttribute paSize = productAttributeRepository.findAll().stream()
                        .filter(pa -> pa.getProduct().getId().equals(p.getId()) && pa.getAttribute().getId().equals(sizeAttr.getId()))
                        .findFirst()
                        .orElseGet(() -> {
                            ProductAttribute pa = new ProductAttribute();
                            pa.setProduct(p);
                            pa.setAttribute(sizeAttr);
                            return productAttributeRepository.save(pa);
                        });

                ProductAttributeValue pavSize = productAttributeValueRepository.findAll().stream()
                        .filter(pav -> pav.getProductAttribute().getId().equals(paSize.getId()) && pav.getAttributeValue().getId().equals(sizeVal.getId()))
                        .findFirst()
                        .orElseGet(() -> {
                            ProductAttributeValue pav = new ProductAttributeValue();
                            pav.setProductAttribute(paSize);
                            pav.setAttributeValue(sizeVal);
                            return productAttributeValueRepository.save(pav);
                        });

                // Create a Variant Option (e.g. Cream, M)
                String variantTitle = assignedColor.getAttributeValue() + ", " + sizeVal.getAttributeValue();
                
                VariantOption variantOption = variantOptionRepository.findAll().stream()
                        .filter(vo -> vo.getProduct().getId().equals(p.getId()) && vo.getTitle().equals(variantTitle))
                        .findFirst()
                        .orElseGet(() -> {
                            VariantOption vo = new VariantOption();
                            vo.setProduct(p);
                            vo.setTitle(variantTitle);
                            vo.setSalePrice(p.getSalePrice() != null ? p.getSalePrice().subtract(new BigDecimal("2.00")) : new BigDecimal("10.00")); // just a test price
                            vo.setComparePrice(p.getComparePrice() != null ? p.getComparePrice() : new BigDecimal("20.00"));
                            vo.setQuantity(100);
                            vo.setActive(true);
                            vo.setImage(finalGallery);
                            return variantOptionRepository.save(vo);
                        });
                
                // In case it already existed without image (from previous run)
                if (variantOption.getImage() == null && finalGallery != null) {
                    variantOption.setImage(finalGallery);
                    variantOptionRepository.save(variantOption);
                }
            }

            // Just creating VariantOption is enough for favorites to display properly.
        }

        return ResponseEntity.ok("Successfully cleared favorites and seeded Colors/Sizes variants for products.");
    }

    private String getColorForProduct(String productName) {
        if (productName == null) return "White";
        switch (productName) {
            case "Chân Váy Midi Xếp Ly": return "Cream";
            case "Đầm Body Cổ V Gợi Cảm": return "Black";
            case "Áo Dệt Kim Tay Ngắn": return "White";
            case "Váy Xòe Họa Tiết Hoa Nhí": return "White";
            case "Đầm Maxi Trễ Vai Đi Biển": return "Orange";
            case "Blazer Ngắn Tay Mùa Hè": return "Cream";
            case "Áo Khoác Blazer Kaki": return "Green";
            case "Váy Sơ Mi Thắt Eo": return "White";
            case "Áo Phao Dáng Ngắn": return "Black";
            case "Áo Cardigan Len Thừng": return "Cream";
            case "Áo Sơ Mi Lụa Cổ V": return "White";
            case "Quần Tây Công Sở Dáng Đứng": return "Black";
            case "Quần Ống Suông Vải Mềm": return "Gray";
            case "Quần Shorts Jeans Gấu Tua Rua": return "Blue";
            case "Quần Jeans Skinny Xanh Đậm": return "Blue";
            case "Áo Măng Tô Kaki": return "Brown";
            case "Chân Váy Jean Ngắn": return "Blue";
            case "Blazer Kẻ Caro Trẻ Trung": return "Caro";
            case "Áo Khoác Len Mỏng Mùa Thu": return "White";
            case "Áo Blazer Pastel Dáng Suông": return "Brown";
            case "Áo Sơ Mi Trắng Basic": return "White";
            case "Quần Short Kaki Cơ Bản": return "Cream";
            case "Áo Len Gân Cổ V": return "White";
            case "Quần Jeans Ống Loe Retro": return "White";
            case "Chân Váy Hoa Nhí Dáng Dài": return "Purple";
            case "Áo Khoác Da Biker": return "Brown";
            case "Đầm Dệt Kim Ôm Body": return "White";
            case "Quần Giả Váy Xếp Ly": return "Brown";
            case "Quần Thể Thao Jogger": return "Gray";
            case "Quần Kaki Nữ Năng Động": return "Cream";
            case "Quần Baggy Lưng Cao": return "Brown";
            case "Áo Khoác Bomber Năng Động": return "Black";
            case "Quần Short Vải Linen Mùa Hè": return "White";
            case "Áo Blouse Công Sở Tay Bồng": return "White";
            case "Áo Cardigan Dáng Dài": return "Cream";
            case "Quần Jeans Rách Phá Cách": return "Blue";
            case "Đầm Dự Tiệc Dáng Dài Xẻ Tà": return "Scarlet";
            case "Áo Khoác Dệt Kim Cài Nút": return "White";
            case "Quần Mom Jeans Lưng Cao": return "Blue";
            case "Chân Váy Chữ A Công Sở": return "Brown";
            case "Quần Short Thể Thao Cotton": return "Black";
            case "Áo Sơ Mi Kẻ Sọc Form Rộng": return "Blue";
            case "Áo Khoác Dạ Dáng Dài": return "White";
            case "Chân Váy Bút Chì Ôm Dáng": return "Black";
            case "Áo Len Cổ Lọ Ấm Áp": return "Cream";
            case "Áo Blouse Trễ Vai Họa Tiết": return "White";
            case "Quần Short Da Thời Trang": return "Brown";
            case "Chân Váy Len Dệt Kim": return "Brown";
            case "Set Bộ Dệt Kim Thanh Lịch": return "White";
            case "Áo Blazer Nữ Đen Classic": return "Black";
            default: return "White";
        }
    }

    private AttributeValue getOrCreateColorValue(Attribute colorAttr, String colorStr) {
        return attributeValueRepository.findAll().stream()
                .filter(v -> v.getAttribute().getId().equals(colorAttr.getId()) && v.getAttributeValue().equalsIgnoreCase(colorStr))
                .findFirst()
                .orElseGet(() -> {
                    AttributeValue v = new AttributeValue();
                    v.setAttribute(colorAttr);
                    v.setAttributeValue(colorStr);
                    return attributeValueRepository.save(v);
                });
    }
}
