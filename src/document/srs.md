# 📋 SRS — Hệ Thống Quản Lý Bệnh Viện

> Java Web Service · RESTful API · Spring Boot + Spring Security + AOP

---

## I. Tổng Quan Hệ Thống

Hệ thống **Backend Stateless** cung cấp RESTful API cho các ứng dụng Client (Web ReactJS/VueJS, Mobile App) vận hành nghiệp vụ bệnh viện.

**Ba trụ cột chính:**

| Trụ cột                         | Mô tả                                  |
| ------------------------------- | -------------------------------------- |
| Quản lý người dùng & Phân quyền | Xác thực qua Spring Security + JWT     |
| Nghiệp vụ cốt lõi               | Đặt lịch khám, quản lý hồ sơ bệnh nhân |
| Giám sát hệ thống               | Logging tách biệt hoàn toàn qua AOP    |

---

## II. Yêu Cầu Chức Năng (Functional Requirements)

| Mã    | Chức năng                                       | Phân quyền             |
| ----- | ----------------------------------------------- | ---------------------- |
| FR-01 | Đăng nhập hệ thống (cấp phát JWT)               | Public                 |
| FR-02 | Xoay vòng Token (Refresh Token)                 | Public                 |
| FR-03 | Đăng xuất (Revoke Token)                        | Authenticated          |
| FR-04 | Đăng ký tài khoản Bệnh nhân mới                 | Public                 |
| FR-05 | Quản lý Người dùng (CRUD, tìm kiếm, phân trang) | Admin                  |
| FR-06 | Đặt lịch khám bệnh                              | Patient                |
| FR-07 | Xem lịch sử khám bệnh cá nhân                   | Patient                |
| FR-08 | Phê duyệt / Từ chối lịch khám                   | Admin, Doctor          |
| FR-09 | Tải lên Hồ sơ bệnh án (Medical Record)          | Doctor                 |
| FR-10 | Đổi mật khẩu / Quên mật khẩu                    | Authenticated / Public |

---

## III. Yêu Cầu Phi Chức Năng (Non-Functional Requirements)

| Nhóm                | Yêu cầu                                                            |
| ------------------- | ------------------------------------------------------------------ |
| **Performance**     | Response time < 2s · Upload file tối đa 10MB/file                  |
| **Security**        | Mật khẩu BCrypt (strength ≥ 10) · JWT ký bằng Secret Key ≥ 256-bit |
| **Availability**    | API uptime mục tiêu ≥ 99%                                          |
| **Scalability**     | Stateless hoàn toàn — không lưu Session/Cookie tại Server          |
| **Maintainability** | Tách layer rõ ràng: Controller → Service → Repository              |
| **Logging**         | Bắt buộc log Request/Response & Exception qua AOP                  |

---

## IV. Bảo Mật (Security Requirements)

### Cơ chế 3 tầng Token

| Token              | Tuổi thọ        | Mục đích                                                  |
| ------------------ | --------------- | --------------------------------------------------------- |
| **AccessToken**    | 15–30 phút      | Gửi kèm mỗi Request trong Header để xác thực người dùng   |
| **RefreshToken**   | 7–30 ngày       | Cấp lại AccessToken mới mà không cần nhập lại mật khẩu    |
| **TokenBlacklist** | Đến hết hạn gốc | Lưu token bị thu hồi khi Logout hoặc Admin khóa tài khoản |

### Ma trận phân quyền API

| Đường dẫn            | Quyền yêu cầu                |
| -------------------- | ---------------------------- |
| `/api/v1/admin/**`   | ADMIN                        |
| `/api/v1/doctor/**`  | DOCTOR                       |
| `/api/v1/patient/**` | PATIENT                      |
| Còn lại              | Public (không cần đăng nhập) |

---

## V. Đặc Tả Use Case

### UC-01 — Đăng nhập & Khởi tạo JWT

**Actor:** Admin, Doctor, Patient · **Endpoint:** `POST /api/auth/login`

**Luồng chính:**

1. Client gửi `{ username, password }` đến `/api/auth/login`
2. Spring Security xác thực mật khẩu BCrypt
3. Hệ thống khởi tạo JWT chứa User ID và Role
4. Trả về `200 OK` + `{ accessToken, refreshToken }`

**Ngoại lệ:**

- Sai tài khoản/mật khẩu → `401 Unauthorized`
- Tài khoản bị khóa → `403 Forbidden`

---

### UC-02 — Quản lý Người dùng (Admin)

**Actor:** Admin · **Endpoint:** `/api/v1/admin/users`

**Luồng chính:**

1. Admin gửi request kèm AccessToken (Role ADMIN)
2. Spring Security Filter xác nhận quyền
3. Service xử lý CRUD qua Spring Data JPA
4. **Bắt buộc dùng Java Stream API** khi ánh xạ Entity → DTO (`stream().map().collect()`)
5. Trả về `200 OK` (query/update) hoặc `201 Created` (tạo mới)

**Ngoại lệ:**

- Không phải ADMIN → `403 Forbidden`
- Dữ liệu thiếu trường bắt buộc → `400 Bad Request`

---

### UC-03 — Đăng xuất & Thu hồi Token

**Actor:** Mọi người dùng đã xác thực · **Endpoint:** `POST /api/auth/logout`

**Luồng chính:**

1. Client gửi request kèm AccessToken trong Header
2. Hệ thống trích xuất JWT, lấy thời gian hết hạn
3. Lưu JWT vào bảng `TokenBlacklist` trong DB
4. Trả về `200 OK` — "Đăng xuất thành công"

**Ngoại lệ:**

- Token đã trong Blacklist hoặc hết hạn → `401 Unauthorized`

---

### UC-04 — Đặt lịch khám & Logging AOP

**Actor:** Patient · **Endpoint:** `POST /api/v1/patient/appointments`

**Luồng chính:**

1. Bệnh nhân gửi `{ doctorId, timeSlot }` để đặt lịch
2. Service tạo `Appointment` với trạng thái `PENDING`, lưu DB
3. **AOP tự động kích hoạt** (`@AfterReturning`) — ghi log: `[INFO] Patient X created Appointment Y`
4. Trả về `201 Created`

**Ngoại lệ:**

- Bác sĩ bị trùng lịch → `409 Conflict` (AOP log qua `@AfterThrowing`)

**Ghi chú quan trọng:** Tuyệt đối không hardcode log bên trong Service — phải dùng AOP Aspect class.

---

### UC-05 — Tải lên Hồ sơ bệnh án (Cloud Storage)

**Actor:** Doctor · **Endpoint:** `POST /api/v1/doctor/records/upload`

**Luồng chính:**

1. Bác sĩ gửi `multipart/form-data` chứa file (ảnh siêu âm, PDF đơn thuốc,...)
2. Spring Security xác thực Token (Role: DOCTOR)
3. Service gọi **Official SDK** của cloud (`cloudinary.uploader().upload()` hoặc `amazonS3.putObject()`)
4. Cloud trả về Secure URL
5. Lưu URL vào trường `fileUrl` của `MedicalRecord` qua JPA
6. Trả về `200 OK` + DTO thông tin hồ sơ

**Ngoại lệ:**

- File sai định dạng hoặc vượt 10MB → `400 Bad Request`
- Lỗi kết nối cloud → `503 Service Unavailable` hoặc `500 Internal Server Error`

---

## VI. Quy Ước Kỹ Thuật

### API Naming & Versioning

- Tất cả API có tiền tố `/api/v1/`
- Tên resource dùng danh từ số nhiều: `/api/v1/users`, `/api/v1/appointments`

### HTTP Status Code

| Code                        | Ý nghĩa                                         |
| --------------------------- | ----------------------------------------------- |
| `200 OK`                    | Truy vấn / cập nhật thành công                  |
| `201 Created`               | Tạo mới thành công                              |
| `204 No Content`            | Xóa thành công                                  |
| `400 Bad Request`           | Lỗi validation dữ liệu đầu vào                  |
| `401 Unauthorized`          | Sai / thiếu Token                               |
| `403 Forbidden`             | Không đủ quyền                                  |
| `409 Conflict`              | Xung đột dữ liệu (trùng lịch, email đã tồn tại) |
| `500 Internal Server Error` | Lỗi hệ thống                                    |

### Chuẩn Response Format

**Thành công:**

```json
{
  "success": true,
  "message": "Created successfully",
  "data": { ... }
}
```

**Lỗi:**

```json
{
    "timestamp": "2026-05-26T14:03:04",
    "status": 400,
    "error": "Bad Request",
    "message": "Email invalid format",
    "path": "/api/v1/users"
}
```

---

## VII. Thuật Ngữ

| Từ viết tắt | Giải thích                                                                  |
| ----------- | --------------------------------------------------------------------------- |
| **JWT**     | JSON Web Token — token mã hóa dùng xác thực người dùng                      |
| **AOP**     | Aspect-Oriented Programming — tách logic phụ (logging) khỏi logic nghiệp vụ |
| **DTO**     | Data Transfer Object — đối tượng truyền dữ liệu, che cấu trúc Entity DB     |
| **RESTful** | Chuẩn thiết kế API dùng HTTP methods (GET, POST, PUT, DELETE)               |
| **BCrypt**  | Thuật toán băm mật khẩu một chiều, không thể giải mã ngược                  |
