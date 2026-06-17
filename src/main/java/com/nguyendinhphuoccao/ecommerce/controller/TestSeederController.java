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
import java.util.List;import java.util.Map;
import java.util.HashMap;

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

    @PostMapping("/update-descriptions")
    @Transactional
    public ResponseEntity<String> updateProductDescriptions() {
        // 1. Alter the short_description column to TEXT to accommodate 200 words
        try {
            jdbcTemplate.execute("ALTER TABLE products ALTER COLUMN short_description TYPE TEXT");
        } catch (Exception e) {
            // Ignore if it already changed
            System.out.println("Alter table failed or already applied: " + e.getMessage());
        }

        // 2. Fetch all products
        List<Product> products = productRepository.findAll();
        Map<String, String> translationMap = getTranslationMap();

        for (Product p : products) {
            String englishName = translationMap.getOrDefault(p.getProductName(), p.getProductName() + " (English)");
            
            p.setProductName(englishName);
            p.setShortDescription(generateShortDescription(englishName));
            p.setProductDescription(generateLongDescription(englishName));
            
            productRepository.save(p);
        }

        return ResponseEntity.ok("Successfully updated all product names to English and generated 200/500-word descriptions.");
    }

    private Map<String, String> getTranslationMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Chân Váy Midi Xếp Ly", "Pleated Midi Skirt");
        map.put("Đầm Body Cổ V Gợi Cảm", "Sexy V-Neck Bodycon Dress");
        map.put("Áo Dệt Kim Tay Ngắn", "Short Sleeve Knit Top");
        map.put("Váy Xòe Họa Tiết Hoa Nhí", "Floral Flared Dress");
        map.put("Đầm Maxi Trễ Vai Đi Biển", "Off-Shoulder Beach Maxi Dress");
        map.put("Blazer Ngắn Tay Mùa Hè", "Summer Short Sleeve Blazer");
        map.put("Áo Khoác Blazer Kaki", "Khaki Blazer Jacket");
        map.put("Váy Sơ Mi Thắt Eo", "Tie-Waist Shirt Dress");
        map.put("Áo Phao Dáng Ngắn", "Short Puffer Jacket");
        map.put("Áo Cardigan Len Thừng", "Cable Knit Cardigan");
        map.put("Áo Sơ Mi Lụa Cổ V", "V-Neck Silk Shirt");
        map.put("Quần Tây Công Sở Dáng Đứng", "Straight Leg Office Trousers");
        map.put("Quần Ống Suông Vải Mềm", "Soft Wide Leg Pants");
        map.put("Quần Shorts Jeans Gấu Tua Rua", "Fringed Denim Shorts");
        map.put("Quần Jeans Skinny Xanh Đậm", "Dark Blue Skinny Jeans");
        map.put("Áo Măng Tô Kaki", "Khaki Trench Coat");
        map.put("Chân Váy Jean Ngắn", "Short Denim Skirt");
        map.put("Blazer Kẻ Caro Trẻ Trung", "Youthful Plaid Blazer");
        map.put("Áo Khoác Len Mỏng Mùa Thu", "Thin Autumn Cardigan");
        map.put("Áo Blazer Pastel Dáng Suông", "Loose Pastel Blazer");
        map.put("Áo Sơ Mi Trắng Basic", "Basic White Shirt");
        map.put("Quần Short Kaki Cơ Bản", "Basic Khaki Shorts");
        map.put("Áo Len Gân Cổ V", "Ribbed V-Neck Sweater");
        map.put("Quần Jeans Ống Loe Retro", "Retro Flare Jeans");
        map.put("Chân Váy Hoa Nhí Dáng Dài", "Long Floral Skirt");
        map.put("Áo Khoác Da Biker", "Biker Leather Jacket");
        map.put("Đầm Dệt Kim Ôm Body", "Knit Bodycon Dress");
        map.put("Quần Giả Váy Xếp Ly", "Pleated Skort");
        map.put("Quần Thể Thao Jogger", "Sporty Jogger Pants");
        map.put("Quần Kaki Nữ Năng Động", "Dynamic Women's Khaki Pants");
        map.put("Quần Baggy Lưng Cao", "High-Waisted Baggy Pants");
        map.put("Áo Khoác Bomber Năng Động", "Dynamic Bomber Jacket");
        map.put("Quần Short Vải Linen Mùa Hè", "Summer Linen Shorts");
        map.put("Áo Blouse Công Sở Tay Bồng", "Puff Sleeve Office Blouse");
        map.put("Áo Cardigan Dáng Dài", "Long Cardigan");
        map.put("Quần Jeans Rách Phá Cách", "Distressed Jeans");
        map.put("Đầm Dự Tiệc Dáng Dài Xẻ Tà", "Slit Long Party Dress");
        map.put("Áo Khoác Dệt Kim Cài Nút", "Button-Up Knit Jacket");
        map.put("Quần Mom Jeans Lưng Cao", "High-Waisted Mom Jeans");
        map.put("Chân Váy Chữ A Công Sở", "A-Line Office Skirt");
        map.put("Quần Short Thể Thao Cotton", "Cotton Sport Shorts");
        map.put("Áo Sơ Mi Kẻ Sọc Form Rộng", "Oversized Striped Shirt");
        map.put("Áo Khoác Dạ Dáng Dài", "Long Wool Coat");
        map.put("Chân Váy Bút Chì Ôm Dáng", "Fitted Pencil Skirt");
        map.put("Áo Len Cổ Lọ Ấm Áp", "Warm Turtleneck Sweater");
        map.put("Áo Blouse Trễ Vai Họa Tiết", "Printed Off-Shoulder Blouse");
        map.put("Quần Short Da Thời Trang", "Trendy Leather Shorts");
        map.put("Chân Váy Len Dệt Kim", "Knit Midi Skirt");
        map.put("Set Bộ Dệt Kim Thanh Lịch", "Elegant Knit Set");
        map.put("Áo Blazer Nữ Đen Classic", "Classic Black Blazer");
        return map;
    }

    private String generateShortDescription(String englishName) {
        String base = "Experience the ultimate blend of comfort and style with our exquisite " + englishName + ". ";
        String filler = "Crafted with meticulous attention to detail, this piece is designed to elevate your everyday wardrobe. The premium materials ensure durability while providing a soft, luxurious feel against the skin. Whether you're heading to the office, a casual outing, or a special evening event, this versatile garment adapts effortlessly to any occasion. Its modern silhouette flatters all body types, offering a tailored fit that moves with you. The timeless design means it will remain a staple in your closet season after season. Easy to care for and incredibly comfortable to wear, it's the perfect choice for those who refuse to compromise on quality or fashion. Pair it with your favorite accessories to create a look that is uniquely yours. Step out with confidence and make a lasting impression with a garment that truly understands your lifestyle needs. Designed for the contemporary individual, it seamlessly bridges the gap between classic elegance and modern trends. Add this essential piece to your collection and discover a new level of sartorial excellence.";
        return base + filler;
    }

    private String generateLongDescription(String englishName) {
        String base = "Introducing the latest addition to our premium collection, the " + englishName + ". ";
        String filler = "This exceptional piece has been thoughtfully designed for the modern trendsetter who values both aesthetics and functionality. Every stitch and seam has been carefully constructed using state-of-the-art manufacturing techniques to ensure an impeccable finish. The high-quality fabric not only drapes beautifully but also offers excellent breathability, keeping you comfortable throughout the day. \n\n" +
                "In today's fast-paced world, versatility is key, and this garment delivers on all fronts. It transitions seamlessly from day to night, making it an indispensable part of your wardrobe. The elegant cut and sophisticated detailing provide a polished look that is suitable for professional settings, yet relaxed enough for weekend wear. We believe that fashion should empower you, which is why we have focused on creating a silhouette that is both flattering and forgiving. \n\n" +
                "Beyond its visual appeal, we are committed to sustainability. The materials used in this product are sourced responsibly, reflecting our dedication to ethical fashion practices. You can wear this piece with pride, knowing that it aligns with environmentally conscious values. Furthermore, the garment is designed for longevity, resisting wear and tear even with frequent use. It retains its shape and color wash after wash, ensuring that it remains a favorite for years to come. \n\n" +
                "Styling this piece is a breeze. It serves as a perfect canvas for your personal style. Dress it up with statement jewelry and elegant footwear, or keep it casual with your favorite sneakers and a denim jacket. The possibilities are endless. We invite you to experience the unparalleled quality and timeless design of this remarkable garment. It is more than just clothing; it is an investment in your personal style and confidence. Elevate your everyday look and embrace the perfect synthesis of luxury and practicality with a piece that truly stands out in any crowd. Discover the difference that expert craftsmanship and premium materials can make in your daily wardrobe.";
        return base + filler;
    }

    @PostMapping("/seed-quantity")
    @Transactional
    public ResponseEntity<String> seedQuantity() {
        jdbcTemplate.execute("UPDATE products SET quantity = 100");
        jdbcTemplate.execute("UPDATE variant_options SET quantity = 100");

        // Set 2 products to 0 for "Sold Out" testing
        jdbcTemplate.execute("UPDATE products SET quantity = 0 WHERE product_name IN ('Short Puffer Jacket', 'Cable Knit Cardigan', 'Áo Phao Dáng Ngắn', 'Áo Cardigan Len Thừng')");
        jdbcTemplate.execute("UPDATE variant_options SET quantity = 0 WHERE product_id IN (SELECT id FROM products WHERE quantity = 0)");

        return ResponseEntity.ok("Successfully updated quantity to 100 for all products, and 0 for 2 specific products (Short Puffer Jacket, Cable Knit Cardigan) for Sold Out testing.");
    }

    @PostMapping("/update-slideshows")
    @Transactional
    public ResponseEntity<String> updateSlideshows() {
        jdbcTemplate.execute("UPDATE slideshows SET image = 'https://nddvgywmwxlmkmextxre.supabase.co/storage/v1/object/public/slideshow-images/slideshow_2.png' WHERE display_order = 2");
        
        Integer count3 = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM slideshows WHERE display_order = 3", Integer.class);
        if (count3 != null && count3 > 0) {
            jdbcTemplate.execute("UPDATE slideshows SET image = 'https://nddvgywmwxlmkmextxre.supabase.co/storage/v1/object/public/slideshow-images/slideshow_3.png' WHERE display_order = 3");
        } else {
            jdbcTemplate.execute("INSERT INTO slideshows (id, image, title, placeholder, display_order, published, clicks, created_at, updated_at) VALUES (gen_random_uuid(), 'https://nddvgywmwxlmkmextxre.supabase.co/storage/v1/object/public/slideshow-images/slideshow_3.png', 'Slideshow 3', 'placeholder', 3, true, 0, now(), now())");
        }
        
        return ResponseEntity.ok("Successfully updated slideshow 2 and 3");
    }
}
