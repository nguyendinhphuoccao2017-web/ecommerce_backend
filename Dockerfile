# Stage 1: Build ứng dụng bằng Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml và tải trước các phụ thuộc (dependencies) để tận dụng cache của Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy toàn bộ mã nguồn và tiến hành build file JAR (bỏ qua chạy thử test để build nhanh hơn)
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Tạo Runtime Image siêu nhẹ chỉ chứa file JAR và Java JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy file JAR đã build từ Stage 1 sang Stage 2
COPY --from=build /app/target/*.jar app.jar

# Cấu hình biến môi trường ép Java tối ưu RAM (Rất quan trọng cho gói 512MB của Render)
ENV JAVA_TOOL_OPTIONS="-Xmx300m -Xms300m -XX:+UseG1GC"

# Mở cổng 8080 (Cổng mặc định của Spring Boot)
EXPOSE 8080

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]