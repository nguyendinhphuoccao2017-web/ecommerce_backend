# Báo Cáo Cập Nhật & Tiến Độ Dự Án (Project Status)

**Ngày cập nhật**: 04/06/2026
**Dự án**: E-commerce Backend (Spring Boot, Java 17, PostgreSQL)

Để tránh việc hệ thống quên đi các thông tin quan trọng trong quá trình phát triển dài hạn, tài liệu này được tạo ra để lưu trữ lại toàn bộ cấu trúc kiến trúc, các quyết định đã chốt và tiến độ công việc đã hoàn thành. Bất kỳ kỹ sư (hay AI) nào đọc dự án sau này đều nên đọc file này đầu tiên.

---

## 1. Thành tựu đã hoàn thành (Hôm nay)

### 1.1 Khởi tạo và Phân tích Cấu trúc
- Đã đọc và phân tích tài liệu `Basement.pdf`, trích xuất chính xác cấu trúc của **34 bảng cơ sở dữ liệu** phục vụ cho hệ thống E-commerce đa dạng (Sản phẩm, Biến thể, Danh mục, Đơn hàng, Vận chuyển, Người dùng, Mã giảm giá, Thẻ, ...).

### 1.2 Khởi tạo Dự án Spring Boot
- Khởi tạo thành công dự án Spring Boot với các dependencies cốt lõi: `Spring Web`, `Spring Data JPA`, `PostgreSQL Driver`, và `Lombok`.
- Cập nhật phiên bản Java trong `pom.xml` xuống **Java 17** để tương thích với môi trường biên dịch máy cục bộ.
- Thiết lập cấu hình kết nối DB qua `application.properties` (Database: `ecommerce_test`, Port: `5432`, `ddl-auto=update`).

### 1.3 Triển khai 34 Bảng sang 5 Tầng Kiến Trúc (Layers)
Mỗi bảng trong 34 bảng (từ `Product`, `Order`, `Category` cho đến `CardItem`, `VariantValue`...) đều đã được ánh xạ thành 5 thành phần Java, tổng cộng **170 tệp Java**:
1. **Entity**: Ánh xạ đầy đủ thuộc tính cơ sở dữ liệu (`@Entity`, `@Table`). Sử dụng `UUID` cho tất cả các Primary Keys và Foreign Keys.
2. **Repository**: Extends `JpaRepository`.
3. **Service (Interface)**: Khai báo chuẩn CRUD cho từng Entity.
4. **ServiceImpl**: Triển khai logic CRUD cơ bản (`@Service`, `@Transactional`).
5. **Controller**: Expose REST APIs mapping chuẩn mực `/api/<entity-plural>`.

### 1.4 Hoàn Thiện & Tối Ưu Hóa JPA Relationships
- **Quan hệ 1-N (Bidirectional `OneToMany`)**: Bổ sung liên kết 2 chiều bằng `List<...>` kèm `@OneToMany(mappedBy = "...")` cho 13 bảng chủ chốt (Ví dụ: `Product` -> `ProductCategory`, `ProductTag`, `Gallery`, `Customer` -> `CustomerAddress`, `Order`).
- **Quan hệ 1-1 (`OneToOne`)**: Chuyển các liên kết mở rộng sang `@OneToOne` (`ProductShippingInfo`, `Sell`, `Card`).
- **Tự tham chiếu (`Self-referencing`)**: Xử lý bảng `Category` (Danh mục cha - con).
- **Phòng chống đệ quy Json**: Áp dụng `@com.fasterxml.jackson.annotation.JsonIgnore` tại các collection ngược (`@OneToMany`) để tránh `StackOverflowError` khi gọi API Controller.
- **Biên dịch**: Đã chạy `.\mvnw clean compile` thành công (BUILD SUCCESS), không tồn tại lỗi cú pháp.

---

## 2. Kiến trúc và Quy ước Code (Code Conventions)
- **ID**: Toàn bộ hệ thống thống nhất sử dụng `UUID` (được tự động sinh bằng cơ chế `UUIDGenerator` của Hibernate).
- **Relationships**: 
  - Ưu tiên giữ lại toàn bộ các Entity trung gian (Ví dụ: `ProductCategory`, `ProductTag`, `ProductSupplier`) và map chúng dưới dạng 2 quan hệ `One-to-Many` (thay vì 1 quan hệ `Many-to-Many` ẩn), giúp giữ được trọn vẹn 34 bảng, tiện lợi khi muốn mở rộng cột trung gian sau này.
  - Các trường audit (`created_at`, `updated_at`, `created_by`, `updated_by`) được map một chiều (`@ManyToOne`) với `StaffAccount`, không map ngược lại để tránh đối tượng `StaffAccount` bị phình to bất thường.
- **Fetch Type**: Toàn bộ quan hệ ManyToOne đều sử dụng `fetch = FetchType.LAZY` để tối ưu hoá hiệu năng truy vấn.

---

## 3. Các bước tiếp theo (Next Steps)
1. **Khởi chạy ứng dụng**: Chạy `.\mvnw spring-boot:run` để Hibernate kết nối Postgres và tự động DDL sinh ra các bảng theo Entity (rất cần thiết để DB và Code đồng bộ).
2. **Tuỳ chỉnh API/Truy vấn nâng cao**: Thêm các câu truy vấn `@Query` trong Repositories và logic nghiệp vụ phức tạp trong Services (ví dụ: Tính toán giỏ hàng, đặt hàng, áp mã giảm giá).
3. **Triển khai GraphQL (Nếu cần)**: Dependency GraphQL đã có sẵn trong `pom.xml`, dự án có thể mở rộng cung cấp GraphQL API nếu Frontend yêu cầu.
4. **Bảo mật**: Triển khai OAuth2 / Spring Security với `StaffAccount` và `Customer`.
