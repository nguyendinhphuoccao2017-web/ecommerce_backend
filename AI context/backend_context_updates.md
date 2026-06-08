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
