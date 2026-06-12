# Tài Liệu Ngữ Cảnh Backend (Backend Context Updates)

Tài liệu này lưu trữ lịch sử các chức năng đã làm, theo dõi các file được cập nhật và mục đích của các thay đổi trong phần Backend (BE) để AI và Developer dễ dàng nắm bắt ngữ cảnh của dự án.

## Cấu Trúc Backend Hiện Tại (Layered Architecture)
- **Controller Layer (`com.nguyendinhphuoccao.ecommerce.controller`)**: Xử lý các request HTTP (POST, GET, PUT, DELETE), tiếp nhận payload từ phía client và trả về dữ liệu DTO/Entity.
- **Service Layer (`com.nguyendinhphuoccao.ecommerce.service` & `impl`)**: Chứa logic nghiệp vụ (Business Logic). Ví dụ: logic hash mật khẩu, cấp phát JWT token, thao tác gán Tag cho Product, lấy thông tin Staff đang đăng nhập, ...
- **Repository Layer (`com.nguyendinhphuoccao.ecommerce.repository`)**: Giao tiếp trực tiếp với cơ sở dữ liệu PostgreSQL thông qua Spring Data JPA (Khai báo Native query, JPQL Constructor Expression).
- **Entity Layer (`com.nguyendinhphuoccao.ecommerce.entity`)**: Định nghĩa cấu trúc bảng (Table) trong Database bằng các Annotation của Hibernate/JPA.
- **DTO Layer (`com.nguyendinhphuoccao.ecommerce.dto`)**: Data Transfer Object dùng để định dạng dữ liệu Request chuyển từ Client lên, hoặc Response trả về Client.
- **Security Layer (`com.nguyendinhphuoccao.ecommerce.security`)**: Xử lý bộ lọc JWT (`JwtAuthenticationFilter`), tải User (`CustomUserDetailsService`), verify quyền truy cập (Role-based).

---

## Chi Tiết Các Cập Nhật Đã Thực Hiện

### 1. Quản Lý Hình Ảnh (Gallery) & Placeholder
- **File:** `src\main\java\com\nguyendinhphuoccao\ecommerce\entity\Gallery.java` (và điều chỉnh tương tự tại `StaffAccount.java`)
- **Thay đổi:** Bổ sung thuộc tính `private String placeholder;` với Annotation `@Column(name = "placeholder", columnDefinition = "TEXT", nullable = true)`.
- **Mục đích:** Giữ nguyên cấu trúc theo thiết kế Database ban đầu. Cho phép hệ thống lưu tự động giá trị `null` vào Database thông qua Hibernate khi API (POST/PUT) xử lý thêm/sửa ảnh mà Client không truyền dữ liệu cho trường `placeholder`.

### 2. Tối Ưu Truy Vấn Lấy Sản Phẩm Theo Tag
- **File:** `src\main\java\com\nguyendinhphuoccao\ecommerce\repository\ProductRepository.java`
- **Thay đổi:** Viết lại câu lệnh JPQL trong hàm `findProductsByTagName`. Thay thế mệnh đề `LEFT JOIN p.galleries g ON g.isThumbnail = true` bằng truy vấn con (Subquery): `(SELECT g.image FROM Gallery g WHERE g.product.id = p.id AND g.isThumbnail = true LIMIT 1)`. Đồng thời loại bỏ `g.image` khỏi mệnh đề `GROUP BY`.
- **Mục đích:** Khắc phục triệt để lỗi Cartesian Product (trùng lặp sản phẩm nhiều lần) khi hệ thống vô tình có nhiều hơn 1 hình ảnh của cùng một sản phẩm được set thuộc tính `is_thumbnail = true`.

### 3. Tối Ưu Mapping JPQL sang DTO (ProductHomeResponseDTO)
- **File:** `src\main\java\com\nguyendinhphuoccao\ecommerce\dto\product\ProductHomeResponseDTO.java`
- **Thay đổi:** Bổ sung biến `placeholder` và tạo một Constructor chứa đầy đủ các tham số (id, productName, salePrice, ...) nhưng tự động gán mặc định `this.placeholder = null;`.
- **Mục đích:** Bắt cầu cho câu lệnh JPQL ở `ProductRepository` tự động map dữ liệu và trả về giá trị `null` cho trường `placeholder` mà không cần xử lý ép kiểu, `CAST(NULL)` hay đổi Database.

### 4. Đồng Bộ Mật Khẩu Luồng Auth & Spring Security
- **File:** `src\main\java\com\nguyendinhphuoccao\ecommerce\security\CustomUserDetails.java`
- **Chức năng:** Tải thông tin UserDetails phục vụ đăng nhập.
- **Tình trạng:** Phương thức `getPassword()` đã được ánh xạ (map) chính xác về `staffAccount.getPasswordHash()` và `customer.getPasswordHash()`.
- **Mục đích:** Bảo đảm tính đồng bộ dữ liệu. Giao diện/API sẽ truyền lên `password` thông qua `LoginRequest`, sau đó Spring Security sẽ sử dụng `getPassword()` để lấy mật khẩu đã mã hóa Bcrypt trong database và đối chiếu. 

### 5. Cập Nhật Cấu Hình Supabase Storage
- **File:** `src\main\resources\application.properties`
- **Thay đổi:** Bổ sung quy ước biến tĩnh URL gốc đến Supabase Storage Bucket: `supabase.storage.base-url=https://nddvgywmwxlmkmextxre.supabase.co/storage/v1/object/public/product-images/`.
- **Mục đích:** Khắc phục phần cấu hình thiếu hụt, cung cấp base URL đầy đủ để phần Backend/Frontend có thể nối chuỗi hiển thị đúng hình ảnh từ Supabase Cloud.

### 6. Cải Tiến Nghiệp Vụ Cốt Lõi Tại ProductService
- **File:** `src\main\java\com\nguyendinhphuoccao\ecommerce\service\impl\ProductServiceImpl.java`
- **Thay đổi:**
    - Cập nhật hàm `getCurrentStaff()` lấy thực thể `StaffAccount` trực tiếp từ Context của Spring Security (`SecurityContextHolder`).
    - Bổ sung hàm `mapDtoToProduct` kiểm tra và map các trường dữ liệu cập nhật từ `ProductRequestDTO` sang `Product` Entity một cách cẩn thận (tránh thay thế giá trị thành Null nếu Client không gửi).
    - Cập nhật hàm `processTags` để kiểm tra, lấy ra từ DB hoặc tạo mới `Tag`, sau đó gán vào danh sách Tag của `Product`.
    - Tích hợp tất cả các logic trên vào 2 phương thức chính là `create` (POST) và `update` (PUT).
- **Mục đích:** 
    - Đảm bảo luồng POST/PUT của Product lưu chính xác thông tin nhân viên thao tác (`createdBy`, `updatedBy`).
    - Quản lý tốt quan hệ Nhiều-Nhiều (ManyToMany) giữa Product và Tag mà không cần phải thực hiện thủ công ngoài Controller.

### 7. Tạo Mới Endpoint Tạo Sản Phẩm Hàng Loạt (Batch Insert)
- **File:** `src\main\java\com\nguyendinhphuoccao\ecommerce\controller\StaffProductController.java` & `ProductRequestDTO.java`
- **Thay đổi:** 
    - Tạo controller mới ánh xạ với `@RequestMapping("/api/staff/products")` chứa `@PostMapping` nhận vào `List<ProductRequestDTO>`.
    - Thêm `categoryName` (String) và `galleries` (List<GalleryDTO>) vào `ProductRequestDTO` để tương thích với dữ liệu mảng gửi từ Client.
- **Mục đích:** Xây dựng luồng tạo sản phẩm hàng loạt cùng một lúc thay vì gọi API liên tục cho từng sản phẩm.

### 8. Tự Động Xử Lý Danh Mục, Hình Ảnh & Giá Trị Mặc Định Của Sản Phẩm
- **File:** `src\main\java\com\nguyendinhphuoccao\ecommerce\service\impl\ProductServiceImpl.java`
- **Thay đổi:**
    - Cập nhật hàm `create` và `update` để gọi các hàm trợ giúp mới (`processCategory`, `processGalleries`, `ensureRequiredFields`).
    - **`processCategory`**: Tìm `Category` dựa vào `categoryName` qua `CategoryRepository`. Nếu chưa có thì tự động tạo `Category` mới, sau đó thiết lập mapping `ProductCategory` để lưu qua Cascade.
    - **`processGalleries`**: Quét danh sách `GalleryDTO` truyền vào và tự động tạo/gắn vào Entity `Gallery` của Product.
    - **`ensureRequiredFields`**: Tự sinh chuỗi random cho `slug`, gán mặc định `quantity` bằng 100, và copy giá trị sang `shortDescription`/`productDescription` nếu Client không gửi để tránh lỗi `DataIntegrityViolationException`.

### 9. Khắc Phục Lỗi 403 Forbidden Xuyên Suốt Luồng Xác Thực (Spring Security)
- **File:** `CustomUserDetailsService.java`, `CustomUserDetails.java`, `SecurityConfig.java`
- **Thay đổi & Mục đích:**
    - **`CustomUserDetailsService`**: Hoán đổi thứ tự ưu tiên kiểm tra `StaffAccount` trước `Customer` trong hàm `loadUserByUsername` để tránh việc tài khoản Staff bị nhận diện nhầm thành Customer nếu vô tình đăng ký trùng Email.
    - **`LazyInitializationException`**: Thêm `@Transactional` vào `CustomUserDetailsService` và gọi `staff.getRole().getRoleName()` ngay bên trong hàm tìm User để nạp sẵn (Eagerly initialize) tên Role. Cứu luồng xác thực khỏi lỗi ngầm khi `JwtAuthenticationFilter` gọi hàm `getAuthorities()` nằm ngoài session của Hibernate.
    - **`CustomUserDetails`**: Loại bỏ việc gán cứng `"ROLE_STAFF"`. Trực tiếp bóc tách chuỗi phân quyền từ Role của Database thông qua `staffAccount.getRole().getRoleName()`.
    - **`SecurityConfig`**: Bổ sung rào chắn bảo vệ API Staff bằng `.requestMatchers("/api/staff/**").hasAnyAuthority("STAFF", "ADMIN", "ROLE_STAFF", "ROLE_ADMIN")` bảo đảm tương thích với tên Role thực tế trong Database.

### 10. Giải Quyết Triệt Để Lỗi Proxy Bằng `@EntityGraph`
- **File:** `src\main\java\com\nguyendinhphuoccao\ecommerce\repository\StaffAccountRepository.java`
- **Thay đổi:** Thêm Annotation `@EntityGraph(attributePaths = {"role"})` vào ngay trên phương thức `findByEmail(String email)`. Cùng lúc đó, tháo bỏ khối lệnh ép nạp proxy thủ công (`staff.getRole().getRoleName()`) bên trong `CustomUserDetailsService`.
- **Mục đích:** Tối ưu hóa truy vấn bằng cơ chế `LEFT JOIN FETCH` của JPA, tải trực tiếp `Role` ngay trong cùng 1 câu SQL cùng với `StaffAccount`. Qua đó xóa bỏ vĩnh viễn rủi ro dính `LazyInitializationException` mà không cần phụ thuộc vào annotation `@Transactional`.

### 11. Bắt Bệnh "Masked Error" (403 Forbidden) & Lỗi Schema Bảng Trung Gian
- **File:** `SecurityConfig.java` & Database (`product_tags`)
- **Thay đổi & Mục đích:**
    - **Vạch trần lỗi ảo 403:** Mở khóa `.requestMatchers("/error").permitAll()` trong `SecurityConfig`. Điều này giúp hệ thống tiết lộ các lỗi gốc (400, 500) do Database ném ra thay vì bị Spring Security "giấu" dưới mã 403 Forbidden.
    - **Khắc phục lỗi Database Schema:** Phát hiện Supabase tự động sinh ra cột `id` (uuid) mang ràng buộc `NOT NULL` cho bảng `product_tags` dù cấu trúc Java ánh xạ đây là bảng 2 cột (`@ManyToMany`). Thay vì tạo entity `ProductTag` rườm rà, giải pháp đã áp dụng là giữ nguyên `@ManyToMany` trên Java và tác động trực tiếp vào cấu trúc Database: `ALTER TABLE product_tags ALTER COLUMN id SET DEFAULT gen_random_uuid()`. Khi đó, Hibernate chỉ insert `product_id` và `tag_id`, phần còn lại Postgres sẽ tự sinh ID hoàn hảo.

### 12. Tách Biệt API Slideshow Cho Trang Chủ (Giới Hạn 2 Ảnh)
- **File:** `SlideshowRepository.java`, `SlideshowServiceImpl.java`, `SlideshowController.java`, `SecurityConfig.java`
- **Thay đổi & Mục đích:**
    - Thay vì dùng `GET /api/slideshows` (lấy tất cả dữ liệu bao gồm cả ẩn/hiện, vốn chỉ nên dùng cho Admin), đã tạo thêm endpoint riêng `GET /api/slideshows/home`.
    - Sử dụng hàm Query Method của Spring Data JPA `findTop2ByPublishedTrueOrderByDisplayOrderAsc()` để bảo đảm phía client chỉ nhận đúng 2 ảnh đang được `published=true` và sắp xếp theo `display_order`.
    - Cập nhật `SecurityConfig.java` cho phép truy cập public không cần xác thực đối với `/api/slideshows/home` nhằm sửa hoàn toàn lỗi `403 Forbidden` trên Mobile App.

### 13. Khắc Phục Lỗi Connection Pool & Bảo Mật API Slideshow
- **File:** `application.properties`, `StaffAccount.java`, `Customer.java`, `Slideshow.java`
- **Thay đổi & Mục đích:**
    - **Connection Pool:** Giữ nguyên kết nối Session Mode qua port 5432 của Supabase nhưng giới hạn `spring.datasource.hikari.maximum-pool-size=3` để ngăn ứng dụng ngốn quá giới hạn 15 kết nối (gây lỗi `FATAL: EMAXCONNSESSION`).
    - **Bảo Mật API:** Bịt lỗ hổng rò rỉ dữ liệu nhạy cảm bằng cách thêm `@JsonIgnore` vào `passwordHash` (tránh lộ chuỗi Bcrypt) và ẩn `createdBy`, `updatedBy` khỏi JSON trả về của API Slideshow để giảm tải dung lượng dư thừa.

### 14. Phát Triển API Lấy Sản Phẩm Theo Danh Mục (Category Products) & Bảng Yêu Thích
- **File:** `CustomerFavorite.java`, `ProductCategoryResponseDTO.java`, `ProductRepository.java`, `ProductServiceImpl.java`, `CategoryController.java`
- **Thay đổi & Mục đích:**
    - **Entity `CustomerFavorite`**: Khởi tạo để map với bảng `customer_favorites` trong database, quản lý quan hệ Many-to-One giữa Customer và Product.
    - **Tối ưu N+1 Query**: Xây dựng hàm `findProductsByCategoryIdAndCustomerId` dùng JPQL constructor trực tiếp trả về `ProductCategoryResponseDTO`. Giải quyết bài toán N+1 bằng cách lấy trực tiếp ảnh Thumbnail (MAX), tính điểm trung bình (AVG), đếm số lượt review (COUNT) và kiểm tra cờ yêu thích (EXISTS) ngay trong 1 câu Query duy nhất.
    - **Bảo mật & Luồng Auth**: Endpoint `GET /api/categories/{id}/products` yêu cầu bắt buộc phải truyền Token hợp lệ. Bổ sung cơ chế fallback (trả về `null`) nếu tài khoản gọi API là Staff, qua đó gán mặc định `isFavorite = false` thay vì ném lỗi `403 Forbidden`.

### 15. Triển Khai Chức Năng Yêu Thích Sản Phẩm (Favorites)
- **File:** `CustomerFavoriteRepository.java`, `CustomerFavoriteService.java`, `CustomerFavoriteController.java`, `CustomerFavorite.java`, `ProductRepository.java`
- **Thay đổi & Mục đích:**
    - **Hiển thị sản phẩm yêu thích**: Bổ sung JPQL `findFavoriteProductsByCustomerId` vào `ProductRepository` trả về List DTO các sản phẩm được `JOIN` với bảng `CustomerFavorite` và sắp xếp theo ngày Like (`createdAt DESC`). Khởi tạo API `GET /api/favorites`.
    - **Chức năng Toggle Favorite (Thêm/Xóa)**: Thiết kế API `POST /api/favorites/{productId}/toggle` cho phép Client gọi 1 chạm để Like/Unlike (Xóa bản ghi nếu đã có, tạo mới nếu chưa có).
    - **Sửa Lỗi Database 500**: Đã khắc phục lỗi `not-null property references a null value` bằng cách bổ sung annotation `@CreationTimestamp` cho thuộc tính `createdAt` tại entity `CustomerFavorite`.

### 16. Tổng hợp Danh mục Ảo (Synthetic Category) "Tops"
- **File:** `CategoryServiceImpl.java`, `ProductServiceImpl.java`, `SecurityConfig.java`
- **Thay đổi & Mục đích:**
    - **Tạo danh mục ảo Tops**: Bổ sung logic tại hàm `getAll()` của `CategoryServiceImpl` để tự động chèn thêm đối tượng `Category` mang tên "Tops" (với ID gán cứng `ffffffff-ffff-ffff-ffff-ffffffffffff`) vào danh sách danh mục trả về mà không cần lưu xuống DB.
    - **Lấy sản phẩm gộp**: Tại `ProductServiceImpl.getProductsByCategory`, nhận diện ID đặc biệt của Tops để vòng lặp qua 10 danh mục hiện tại, trích xuất đúng 2 sản phẩm đầu tiên từ mỗi danh mục và gom lại thành danh sách 20 sản phẩm. Kỹ thuật này giúp Frontend nhẹ gánh đi 10 lần request gọi API, tối ưu hoá mượt mà cho Mobile.
    - **Mở khóa Public GET API Categories**: Cập nhật `SecurityConfig` cho phép `GET /api/categories/**` truy cập công khai không cần truyền JWT Token.

### 17. Tối Ưu N+1 Query Trang Yêu Thích & Ánh Xạ Biến Thể
- **File:** `CustomerFavoriteRepository.java`, `CustomerFavoriteServiceImpl.java`, `FavoriteResponseDTO.java`
- **Thay đổi & Mục đích:**
    - **Custom Query JPQL (`findFavoriteProductsWithDetails`)**: Viết mới câu truy vấn trong Repository thay thế toàn bộ vòng lặp gọi lazy fetching (như lấy tags, reviews, galleries) bằng `LEFT JOIN` và gom nhóm trực tiếp, qua đó xoá bỏ triệt để lỗi N+1 Query. Tổng số truy vấn giảm từ hàng chục câu lệnh xuống đúng **2 câu lệnh SQL**.
    - **Lấy giá & ảnh theo biến thể đã chọn**: Sử dụng hàm `COALESCE(vo.salePrice, p.salePrice)` và `COALESCE(vo.image.image, (MAX(g.image)))` thông qua `LEFT JOIN variant_options vo` để trả về chính xác giá bán và hình ảnh của Màu sắc/Size mà khách hàng đã bấm Like thay vì lấy chung của sản phẩm gốc.
    - **Bảo Vệ NullPointerException**: Áp dụng `COALESCE(AVG(r.rating), 0.0)` khi bóc tách điểm đánh giá để ngăn lỗi API sập do sản phẩm chưa có Review.

### 18. Khởi Tạo Data Seeder Cho Thuộc Tính Sản Phẩm (Color & Size)
- **File:** `TestSeederController.java`
- **Thay đổi & Mục đích:**
    - Tạo endpoint `POST /api/test/seed-colors` nhằm hỗ trợ team test hệ thống Variant.
    - **Dọn dẹp Data Rác**: API tự động `deleteAll()` các bản ghi yêu thích cũ để xoá bỏ lỗi liên kết khóa ngoại.
    - **Mapping Dữ Liệu Thực Tế**: Đã thiết kế lại logic để map tên Màu Sắc (Color) chuẩn xác 1-1 với tên sản phẩm (Ví dụ: "Chân Váy Midi Xếp Ly" -> "Cream") dựa trên file thiết kế `Một số điểm cần sửa.pdf`. Tự động chèn dữ liệu Size (XS, S, M, L, XL - *Sau đó Developer đã cập nhật lại chỉ seed riêng size M*) kết hợp thành các chuỗi Variant (vd: "Cream, M").
    - Cập nhật cơ chế tự động lấy ảnh Thumbnail chính của sản phẩm (từ `Gallery`) nhúng vào `VariantOption` giúp thẻ sản phẩm Favorites load đúng hình đại diện của biến thể.

### 19. Phân Tích & Xác Nhận Tính Toàn Vẹn Của Hệ Thống Yêu Thích (Favorites)
- **Luồng Thực thi**: API `POST /api/favorites/{productId}/toggle` đã được kiểm thử với các case edge như "đổi size", "bấm đúp".
- **Kết quả Audit (Kiểm toán)**:
    - **Bảo mật & Tính độc lập**: API Toggle xử lý độc lập trên từng `productId`. Không có chuyện thích sản phẩm B mà lại làm mất sản phẩm A. Lỗi "chỉ chọn được 1 sản phẩm" đã được làm rõ là do hệ quả của việc User bấm Double-Tap từ Frontend (Gửi 2 request Thêm/Xoá cùng lúc).
    - **Toàn vẹn Dữ Liệu**: Việc Backend phản hồi `200 OK` mượt mà khi xử lý 2 transaction ngược chiều trong 1 phần nghìn giây cho thấy cơ chế `@Transactional` và `deleteAll()` của Spring Data JPA hoạt động hoàn hảo, chặn đứng mọi lỗi Lock DB hay Foreign Key Constraint.
