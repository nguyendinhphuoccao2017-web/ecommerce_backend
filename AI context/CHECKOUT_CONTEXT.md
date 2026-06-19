# BACKEND CHECKOUT & BAG SYSTEM CONTEXT

*Last Updated: 2026-06-19*

## 1. Giỏ Hàng (Cart)
- **Service:** `CardServiceImpl.java` -> Hàm `getCart(UUID customerId)` trả về danh sách chi tiết thay vì Entity, giúp ánh xạ trực tiếp sang `CartResponse` bên FE.
- **Tính toán:** `subtotal` được tính chính xác thông qua giá của `VariantOption` (salePrice/comparePrice) hoặc fallback về giá gốc của `Product`.

## 2. Thanh Toán (Checkout)
- **Logic:** `CheckoutServiceImpl.java` xử lý toàn bộ quá trình thanh toán, tiếp nhận `CheckoutRequestDTO` (bao gồm `paymentMethod`, `shippingAddressId`, `deliveryMethod`, `couponCode`).
- **Pessimistic Locking:** Sử dụng `@Lock(LockModeType.PESSIMISTIC_WRITE)` trên `ProductRepository` & `VariantOptionRepository` để tránh Race Condition (nhiều người mua cùng 1 sản phẩm cuối cùng vào cùng thời điểm).
- **Bug Fix:** Lỗi `500 Internal Server Error (IdentifierGenerationException)` đã được khắc phục triệt để bằng cách tự động sinh `UUID.randomUUID().toString()` cho trường ID của bảng `orders` (kiểu String) trước khi chạy hàm `.save()`.

## 3. Quản lý Địa Chỉ (Customer Addresses)
- **Soft Delete:** Hàm `delete()` trong `CustomerAddressServiceImpl` được thiết kế dưới dạng Soft Delete (`isActive = false`). Do ràng buộc thiết kế Data Model gốc, không thể xoá cứng địa chỉ để giữ liên kết dữ liệu lịch sử của `Order`.
- **API Mới:** Bổ sung API `GET /api/customer-addresses/my-addresses` chuyên trả về các địa chỉ đang Active của khách hàng đang đăng nhập.

## 4. Xử lý Bất Đồng Bộ (Async Notifications)
- **Service:** `NotificationServiceImpl.java` ứng dụng `@Async` của Spring Boot để gửi Email Xác nhận (giả lập) ngay sau khi Submit Order thành công mà không block luồng xử lý chính.

## 5. System Sync
- **Trạng thái:** Toàn bộ RESTful API liên quan đến Giỏ Hàng và Thanh Toán đã được tích hợp thành công, đồng bộ hoá dữ liệu và hoạt động ổn định với ứng dụng Frontend (Flutter).
