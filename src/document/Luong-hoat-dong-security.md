User Login Request
(username + password)
        │
        ▼
SecurityFilterChain
(UsernamePasswordAuthenticationFilter)
        │
        │ tạo UsernamePasswordAuthenticationToken
        ▼
AuthenticationManager
        │
        │ gọi authenticate()
        ▼
AuthenticationProvider
(DaoAuthenticationProvider)
        │
        ├── UserDetailsService
        │       │
        │       ▼
        │   Database
        │       │
        │       ▼
        │   UserDetails
        │
        └── PasswordEncoder
                │
                ▼
      So sánh password nhập vào
      với password đã hash trong DB
        │
        ▼
Xác thực thành công
        │
        ▼
Authentication
(
 principal = UserDetails,
 authorities = Roles,
 authenticated = true
)
        │
        ▼
Lưu vào SecurityContext
        │
        ▼
Controller