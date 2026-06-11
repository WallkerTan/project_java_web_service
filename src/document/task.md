# 🗓️ Kế Hoạch Triển Khai — Hospital Management API

> Tổng điểm: 100 | Thời gian: 4 ngày

---

## 📊 Tổng quan điểm

| Ngày  | Nội dung                                        | Điểm |
| ----- | ----------------------------------------------- | ---- |
| Day 1 | Nền tảng (Setup + FR-04, FR-05, FR-06)          | 25   |
| Day 2 | Auth & Nghiệp vụ (FR-01 → FR-03, FR-07 → FR-10) | 35   |
| Day 3 | Nâng cao (FR-11, FR-12, FR-13)                  | 40   |
| Day 4 | Hoàn thiện, test, fix bug                       | —    |

---

## 🟢 DAY 1 — Nền tảng & Nghiệp vụ cơ bản (25 điểm)

### ⚙️ Setup dự án (không tính điểm — bắt buộc làm trước)

- [ ] Tạo project Spring Boot (Spring Initializr)
    - Dependencies: `Spring Web`, `Spring Security`, `Spring Data JPA`, `Lombok`, `Validation`
    - Database: MySQL hoặc PostgreSQL
- [ ] Cấu hình `application.yml`
    - Datasource (host, port, username, password)
    - JWT secret key + expiration time
- [ ] Tạo cấu trúc package:
    ```
    com.hospital
    ├── config/         ← Security config, JWT config
    ├── controller/
    ├── service/
    ├── repository/
    ├── entity/
    ├── dto/
    ├── exception/
    └── aspect/        ← AOP (dùng ở Day 3)
    ```
- [ ] Tạo các Entity cơ bản + migrate DB:
    - `User` (id, username, email, password, role, status, createdAt)
    - `Appointment` (id, patientId, doctorId, timeSlot, status, createdAt)
    - `MedicalRecord` (id, appointmentId, diagnosis, createdAt)
    - `TokenBlacklist` (id, token, expiredAt)

---

### FR-04 — Đăng ký tài khoản Bệnh nhân (10 điểm)

**Endpoint:** `POST /api/v1/auth/register`

- [ ] Tạo `RegisterRequest` DTO (username, email, password, fullName, phone)
- [ ] Validate input: `@NotBlank`, `@Email`, `@Size(min=8)` cho password
- [ ] Service: kiểm tra email/username đã tồn tại chưa → ném `409 Conflict`
- [ ] Mã hóa password bằng `BCryptPasswordEncoder` (strength 10)
- [ ] Gán role mặc định `PATIENT`, status `ACTIVE`
- [ ] Lưu DB, trả về `201 Created` + UserDTO (không trả password)

**Test thủ công:**

```json
POST /api/v1/auth/register
{ "username": "john", "email": "john@mail.com", "password": "Pass@1234" }
```

---

### FR-05 — Quản lý Người dùng CRUD (5 điểm)

**Endpoints:** `GET/POST/PUT /api/v1/admin/users`

- [ ] `GET /api/v1/admin/users?page=0&size=10` — Danh sách có phân trang (`Pageable`)
- [ ] `GET /api/v1/admin/users/{id}` — Xem chi tiết 1 user
- [ ] `POST /api/v1/admin/users` — Tạo user mới (Admin tạo Doctor)
- [ ] `PUT /api/v1/admin/users/{id}` — Cập nhật thông tin
- [ ] `DELETE /api/v1/admin/users/{id}` — Deactivate (không xóa thật, đổi status = INACTIVE)
- [ ] **Bắt buộc:** Dùng `Stream API` khi map List Entity → List DTO:
    ```java
    return users.stream()
        .map(this::toDto)
        .collect(Collectors.toList());
    ```

---

### FR-06 — Đặt lịch khám bệnh (10 điểm)

**Endpoint:** `POST /api/v1/patient/appointments`

- [ ] Tạo `AppointmentRequest` DTO (doctorId, timeSlot)
- [ ] Service: kiểm tra bác sĩ có bị trùng lịch giờ đó không → ném `409 Conflict`
- [ ] Tạo Appointment với status = `PENDING`
- [ ] Lưu DB, trả về `201 Created` + AppointmentDTO

**Trạng thái Appointment:**

```
PENDING → APPROVED → COMPLETED
         → REJECTED
```

---

## 🔵 DAY 2 — Auth & Các chức năng còn lại (35 điểm)

### FR-01 — Đăng nhập & cấp JWT (5 điểm)

**Endpoint:** `POST /api/v1/auth/login`

- [ ] Tạo `LoginRequest` DTO (username, password)
- [ ] Cấu hình `SecurityFilterChain` — whitelist các route public
- [ ] Implement `UserDetailsService` — load user từ DB theo username
- [ ] Viết `JwtUtil`:
    - `generateAccessToken(user)` — expire 15 phút
    - `generateRefreshToken(user)` — expire 7 ngày
    - `validateToken(token)` → true/false
    - `extractUsername(token)` → String
- [ ] Viết `JwtAuthFilter extends OncePerRequestFilter`:
    - Đọc token từ header `Authorization: Bearer <token>`
    - Validate → set `SecurityContextHolder`
    - Kiểm tra token có trong `TokenBlacklist` không
- [ ] Controller trả `200 OK` + `{ accessToken, refreshToken }`
- [ ] Exception: sai mật khẩu → `401`, tài khoản inactive → `403`

---

### FR-02 — Refresh Token (5 điểm)

**Endpoint:** `POST /api/v1/auth/refresh`

- [ ] Nhận `{ refreshToken }` từ body
- [ ] Validate RefreshToken (chưa hết hạn, không trong blacklist)
- [ ] Sinh `accessToken` mới, trả về `200 OK`
- [ ] Exception: refresh token hết hạn/invalid → `401`

---

### FR-03 — Đăng xuất & Blacklist Token (5 điểm)

**Endpoint:** `POST /api/v1/auth/logout`

- [ ] Đọc token từ Header
- [ ] Lưu token vào bảng `TokenBlacklist` (lưu cả `expiredAt`)
- [ ] `JwtAuthFilter` kiểm tra blacklist trước khi xác thực
- [ ] Trả về `200 OK` + message "Đăng xuất thành công"
- [ ] Định kỳ xóa token đã hết hạn khỏi blacklist (`@Scheduled`)

---

### FR-07 — Xem lịch sử khám bệnh (5 điểm)

**Endpoint:** `GET /api/v1/patient/appointments`

- [ ] Lấy patientId từ SecurityContext (không cần truyền param)
- [ ] Hỗ trợ phân trang + filter theo status
- [ ] Trả về danh sách AppointmentDTO (dùng Stream API)

---

### FR-08 — Phê duyệt / Từ chối lịch khám (5 điểm)

**Endpoint:** `PUT /api/v1/doctor/appointments/{id}/status`

- [ ] Body: `{ "status": "APPROVED" }` hoặc `{ "status": "REJECTED" }`
- [ ] Validate: chỉ cho phép đổi từ `PENDING` → `APPROVED` hoặc `REJECTED`
- [ ] Trả về `200 OK` + AppointmentDTO đã cập nhật

---

### FR-09 — Tải lên hồ sơ bệnh án (5 điểm)

**Endpoint:** `POST /api/v1/doctor/records/upload`

- [ ] Nhận `MultipartFile` qua `multipart/form-data`
- [ ] Validate: chỉ chấp nhận PDF, JPG, PNG — max 10MB
- [ ] Upload lên Cloudinary (hoặc AWS S3) qua Official SDK
- [ ] Lưu `fileUrl` trả về vào bảng `MedicalRecord`
- [ ] Exception: lỗi upload cloud → `503 Service Unavailable`

---

### FR-10 — Đổi mật khẩu / Quên mật khẩu (5 điểm)

**Đổi mật khẩu** — `PUT /api/v1/auth/change-password`

- [ ] Nhận `{ oldPassword, newPassword }`
- [ ] Verify oldPassword với BCrypt trước khi đổi
- [ ] Encode newPassword và lưu DB

**Quên mật khẩu** — `POST /api/v1/auth/forgot-password`

- [ ] Nhận `{ email }`, tạo reset token (UUID), lưu DB với TTL 15 phút
- [ ] Gửi email chứa link reset (dùng Spring Mail hoặc log ra console nếu chưa config)

---

## 🟠 DAY 3 — Tính năng nâng cao (40 điểm)

### FR-11 — AOP Logging thời gian thực thi (10 điểm)

- [ ] Tạo annotation `@LogExecutionTime`
- [ ] Tạo `LoggingAspect`:
    ```java
    @Aspect @Component
    public class LoggingAspect {
        @Around("@annotation(LogExecutionTime)")
        public Object logTime(ProceedingJoinPoint pjp) throws Throwable {
            long start = System.currentTimeMillis();
            Object result = pjp.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("[{}] executed in {} ms", pjp.getSignature().getName(), duration);
            return result;
        }
    }
    ```
- [ ] Annotate **tất cả** Service methods với `@LogExecutionTime`
- [ ] Thêm `@AfterThrowing` để log exception:
    ```java
    @AfterThrowing(pointcut = "execution(* com.hospital.service.*.*(..))", throwing = "ex")
    public void logError(JoinPoint jp, Exception ex) {
        log.error("[{}] threw: {}", jp.getSignature().getName(), ex.getMessage());
    }
    ```
- [ ] Verify: chạy API → thấy log `[createAppointment] executed in 45 ms`

---

### FR-12 — Unit Test (20 điểm)

**5 Service Tests** (`src/test/java/.../service/`)

- [ ] `UserServiceTest` — test `createUser()`: mock repo, verify BCrypt called
- [ ] `AppointmentServiceTest` — test `createAppointment()`: mock repo, test conflict case
- [ ] `AppointmentServiceTest` — test `updateStatus()`: test transition PENDING→APPROVED
- [ ] `AuthServiceTest` — test `login()`: mock UserDetailsService + JwtUtil
- [ ] `MedicalRecordServiceTest` — test `uploadRecord()`: mock Cloudinary SDK

**5 Controller Tests** (`src/test/java/.../controller/`) dùng `@WebMvcTest` + `MockMvc`

- [ ] `AuthControllerTest` — `POST /api/v1/auth/login` với credentials đúng → 200
- [ ] `AuthControllerTest` — `POST /api/v1/auth/login` với password sai → 401
- [ ] `UserControllerTest` — `GET /api/v1/admin/users` không có token → 401
- [ ] `AppointmentControllerTest` — `POST /api/v1/patient/appointments` → 201
- [ ] `AppointmentControllerTest` — `PUT /api/v1/doctor/appointments/{id}/status` → 200

---

### FR-13 — Redis TokenBlacklist (10 điểm)

- [ ] Thêm dependency: `spring-boot-starter-data-redis`
- [ ] Cấu hình Redis trong `application.yml`:
    ```yaml
    spring:
        redis:
            host: localhost
            port: 6379
    ```
- [ ] Tạo `RedisTokenBlacklistService`:

    ```java
    // Lưu token vào Redis với TTL = thời gian còn lại của token
    redisTemplate.opsForValue().set("blacklist:" + token, "revoked", ttl, TimeUnit.MILLISECONDS);

    // Kiểm tra token có trong blacklist không
    Boolean exists = redisTemplate.hasKey("blacklist:" + token);
    ```

- [ ] Thay thế logic check DB trong `JwtAuthFilter` bằng `RedisTokenBlacklistService`
- [ ] **Xóa** bảng `TokenBlacklist` trong DB (không cần nữa)
- [ ] Chạy Redis local: `docker run -d -p 6379:6379 redis`
- [ ] Test: login → logout → dùng lại token cũ → phải nhận `401`

---

## ✅ DAY 4 — Hoàn thiện & Gỡ lỗi

### Kiểm tra toàn bộ flow

- [ ] Chạy đầy đủ từng FR từ Day 1 → Day 3
- [ ] Test các luồng lỗi (sai token, thiếu quyền, trùng lịch, file quá lớn)
- [ ] Đảm bảo tất cả response đúng chuẩn format:
    ```json
    { "success": true, "message": "...", "data": { ... } }
    ```

### Global Exception Handler

- [ ] Tạo `@RestControllerAdvice` xử lý tập trung:
    - `BadCredentialsException` → 401
    - `AccessDeniedException` → 403
    - `MethodArgumentNotValidException` → 400 (kèm field errors)
    - `EntityNotFoundException` → 404
    - `ConflictException` (custom) → 409
    - `Exception` (catch-all) → 500

### Security Config kiểm tra lần cuối

- [ ] Public routes: `/api/v1/auth/**`
- [ ] Admin routes: `/api/v1/admin/**` → role `ADMIN`
- [ ] Doctor routes: `/api/v1/doctor/**` → role `DOCTOR`
- [ ] Patient routes: `/api/v1/patient/**` → role `PATIENT`

### Tài liệu & dọn dẹp

- [ ] Viết `README.md`: hướng dẫn chạy dự án, env vars cần thiết
- [ ] Xóa code debug/hardcode
- [ ] Đảm bảo không có password, API key trong code (dùng env hoặc `.env`)
- [ ] Chạy `mvn test` — tất cả 10 unit test phải pass

---

## 📝 Ghi chú kỹ thuật quan trọng

| Vấn đề                                | Giải pháp                                                   |
| ------------------------------------- | ----------------------------------------------------------- |
| Không để lộ cấu trúc Entity           | Luôn dùng DTO để trả response                               |
| Map List → phải dùng Stream           | `list.stream().map(dto::from).collect(Collectors.toList())` |
| Log không được hardcode trong Service | Dùng AOP Aspect class                                       |
| Token bị lộ sau logout                | Blacklist bằng Redis (TTL tự động)                          |
| File upload quá lớn                   | Validate size trước khi gọi SDK cloud                       |
