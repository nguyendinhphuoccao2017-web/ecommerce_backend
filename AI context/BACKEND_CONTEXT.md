# TÀI LIỆU LƯU TRỮ NGỮ CẢNH BACKEND (BACKEND CONTEXT)

Tài liệu này được tạo ra để lưu trữ toàn bộ cấu trúc Backend hiện tại, tổng hợp các chức năng đã làm, cũng như giải thích chi tiết mục đích của từng thay đổi để Gemini và đội ngũ Developer dễ dàng theo dõi tiến độ dự án mà không sợ bị mất ngữ cảnh (context).

---

## 1. TỔNG QUAN CẤU TRÚC BACKEND (BE STRUCTURE)
Dự án được triển khai theo kiến trúc 5 lớp (5-Layer Architecture) chuẩn của Spring Boot REST API, bao gồm 34 bảng dữ liệu quản lý toàn diện E-Commerce:

*   **`controller`**: Chứa 34 file Controller (ví dụ: `ProductController.java`, `GalleryController.java`, `AuthController.java`...). Đây là lớp ngoài cùng, tiếp nhận các request HTTP (GET, POST, PUT, DELETE) từ Frontend Flutter, điều hướng dữ liệu đến Service và trả về response cho Client.
*   **`service` & `service/impl`**: Lớp xử lý nghiệp vụ cốt lõi (Business Logic). Mọi thao tác phức tạp như: tính toán, kết nối các bảng, xử lý mã hóa mật khẩu, kiểm tra quyền hạn, gán người tạo (createdBy), thao tác Tag... đều nằm ở đây. (VD: `ProductServiceImpl.java`, `AuthServiceImpl.java`).
*   **`repository`**: Tầng giao tiếp cơ sở dữ liệu PostgreSQL qua Spring Data JPA. Các interface ở đây (`ProductRepository`, `GalleryRepository`...) kế thừa `JpaRepository` và chứa các câu lệnh JPQL hoặc Native SQL Query tùy chỉnh.
*   **`entity`**: Chứa 34 file ánh xạ (ORM mapping) trực tiếp với 34 bảng trong cơ sở dữ liệu bằng các annotation Hibernate (VD: `@Entity`, `@Table`, `@Column`, `@OneToMany`, `@ManyToMany`).
*   **`dto` (Data Transfer Object)**: Định nghĩa các object trung gian chứa dữ liệu tinh gọn dùng để trao đổi giữa FE và BE. Nhờ DTO, hệ thống không trả về toàn bộ dữ liệu nhạy cảm của Entity (như giá nhập hàng `buyingPrice` hay `passwordHash`).
*   **`security`**: Lớp chứa cấu hình phân quyền (Role-based access), xử lý bộ lọc JWT Token (`JwtAuthenticationFilter`), và xác thực User (`CustomUserDetailsService`).

---

## 2. NHỮNG CHỨC NĂNG ĐÃ THAY ĐỔI & CẬP NHẬT TRỌNG TÂM

Dưới đây là lịch sử các file cụ thể đã được thay đổi, chức năng của file đó, nội dung cập nhật, và mục đích của việc cập nhật:

### 2.1. Tối ưu Entity và cơ chế lưu Null cho Placeholder
*   **File bị thay đổi**: 
    *   `src/main/java/com/nguyendinhphuoccao/ecommerce/entity/Gallery.java`
    *   `src/main/java/com/nguyendinhphuoccao/ecommerce/entity/StaffAccount.java`
*   **Chức năng của file**: Ánh xạ bảng `gallery` (quản lý hình ảnh) và `staff_accounts` (nhân viên) trong DB.
*   **Nội dung cập nhật**: 
    *   Thêm thuộc tính `private String placeholder;` vào `Gallery.java`.
    *   Cấu hình Explicit nullable (`nullable = true`) cho cột `image` và `placeholder` trong `StaffAccount.java`.
*   **Mục đích cập nhật**: Bảo toàn cấu trúc Database theo đúng thiết kế ban đầu. Cấu hình này giúp hệ thống tự động lưu giá trị `null` xuống Database (thông qua Hibernate) khi các API POST/PUT thêm hoặc sửa ảnh/nhân viên mà Client không gửi dữ liệu cho trường `placeholder`.

### 2.2. Khắc phục lỗi Cartesian Product (Trùng lặp hình ảnh)
*   **File bị thay đổi**: `src/main/java/com/nguyendinhphuoccao/ecommerce/repository/ProductRepository.java`
*   **Chức năng của file**: Giao tiếp Database để lấy danh sách sản phẩm.
*   **Nội dung cập nhật**: Viết lại câu lệnh JPQL trong hàm `findProductsByTagName`. Thay thế mệnh đề `LEFT JOIN p.galleries g ON g.isThumbnail = true` gây rủi ro cao bằng truy vấn con (Subquery): `(SELECT g.image FROM Gallery g WHERE g.product.id = p.id AND g.isThumbnail = true LIMIT 1)`. Đồng thời loại bỏ `g.image` khỏi mệnh đề `GROUP BY`.
*   **Mục đích cập nhật**: Chống lỗi nhân bản dòng (Cartesian Product) khiến sản phẩm bị lặp lại nhiều lần ở màn hình Home Screen khi vô tình có nhiều hơn 1 hình ảnh của cùng một sản phẩm được set thuộc tính `is_thumbnail = true`. Subquery với `LIMIT 1` đảm bảo chỉ lấy đúng 1 ảnh đại diện.

### 2.3. Tối ưu JPQL Mapping DTO (Xử lý trường Placeholder)
*   **File bị thay đổi**: `src/main/java/com/nguyendinhphuoccao/ecommerce/dto/product/ProductHomeResponseDTO.java`
*   **Chức năng của file**: Object chuẩn hóa dữ liệu danh sách sản phẩm để gửi về Home Screen trên app Flutter.
*   **Nội dung cập nhật**: Bổ sung biến `placeholder` và tạo một hàm khởi tạo (Constructor) riêng biệt. Trong Constructor này, tham số truyền vào là 7 biến cơ bản, biến thứ 8 (`placeholder`) được tự động gán cứng `this.placeholder = null;`.
*   **Mục đích cập nhật**: Hỗ trợ câu lệnh JPQL Constructor Expression bên trong `ProductRepository` tự động map và trả về giá trị `null` cho trường `placeholder` mà không cần phải thực hiện ép kiểu `CAST(NULL AS string)` trực tiếp trên SQL, tránh gây lỗi không tương thích Hibernate.

### 2.4. Đồng Bộ Mật Khẩu (PasswordHash) cho Luồng Đăng Nhập
*   **File bị thay đổi / kiểm tra**: `src/main/java/com/nguyendinhphuoccao/ecommerce/security/CustomUserDetails.java`
*   **Chức năng của file**: Lớp wrapper bọc đối tượng User (`Customer` hoặc `StaffAccount`) để Spring Security có thể hiểu và kiểm tra quyền hạn.
*   **Nội dung cập nhật / trạng thái**: Đảm bảo phương thức `getPassword()` của Spring Security được map ánh xạ chính xác về hàm `getPasswordHash()` của đối tượng thực thể.
*   **Mục đích cập nhật**: Frontend gửi lên thuộc tính `password` trong DTO `LoginRequest`. Spring Security bắt lấy và gọi hàm `getPassword()` để lấy mật khẩu đã mã hóa Bcrypt trong database ra để so sánh. Cấu trúc hiện tại giúp 2 bên bất đồng bộ tên gọi (`password` vs `passwordHash`) nhưng vẫn kết nối xác thực hoàn hảo. Lỗi đỏ trên IDE chỉ là do cache của Lombok, toàn bộ dự án vẫn build thành công.

### 2.5. Xử Lý Luồng Nghiệp Vụ Cốt Lõi Của Sản Phẩm (ProductService)
*   **File bị thay đổi**: `src/main/java/com/nguyendinhphuoccao/ecommerce/service/impl/ProductServiceImpl.java`
*   **Chức năng của file**: Xử lý logic Thêm/Sửa/Xóa sản phẩm.
*   **Nội dung cập nhật**:
    *   Thêm hàm `getCurrentStaff()` để trích xuất `StaffAccount` từ `SecurityContextHolder`.
    *   Tách hàm `mapDtoToProduct()` để mapping dữ liệu an toàn.
    *   Tách hàm `processTags()` để thao tác gán nhãn Tag (Tìm xem Tag đã có chưa, chưa có thì tạo mới, và lưu quan hệ ManyToMany).
*   **Mục đích cập nhật**: Đảm bảo tính minh bạch. Khi gọi API `POST` hoặc `PUT` sản phẩm, hệ thống sẽ tự động gán tài khoản nhân viên đang đăng nhập vào các trường `createdBy` và `updatedBy`. Việc tách hàm giúp code Clean, dễ mở rộng và bảo trì sau này.

### 2.6. Cập Nhật Cấu Hình Kết Nối Supabase Storage
*   **File bị thay đổi**: `src/main/resources/application.properties`
*   **Chức năng của file**: Chứa các biến môi trường và cấu hình Database, Security, Mail của toàn hệ thống.
*   **Nội dung cập nhật**: Bổ sung quy ước biến: `supabase.storage.base-url=https://nddvgywmwxlmkmextxre.supabase.co/storage/v1/object/public/product-images/`.
*   **Mục đích cập nhật**: Khắc phục việc thiếu cấu hình môi trường. Cung cấp URL cơ sở (Base URL) cho Supabase Storage Bucket để Backend hoặc Frontend dễ dàng nối chuỗi với tên hình ảnh (`image.jfif`), nhằm hiển thị chính xác ảnh từ Cloud.

---
*Ghi chú: Lịch sử trên ghi nhận trạng thái Backend tới thời điểm hiện tại. Khi có chức năng mới, vui lòng yêu cầu AI cập nhật tiếp vào file này.*
