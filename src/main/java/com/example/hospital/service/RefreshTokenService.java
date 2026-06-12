package com.example.hospital.service;

import com.example.hospital.model.entity.RefreshToken;
import com.example.hospital.model.entity.User;

public interface RefreshTokenService {

    // Tạo refresh token mới khi login
    RefreshToken createRefreshToken(User user);

    RefreshToken createRefreshToken2(User user,String token);

    // Kiểm tra token còn hạn và chưa bị revoke
    RefreshToken verifyRefreshToken(String token);

    // Lấy refresh token theo chuỗi token
    RefreshToken findByToken(String token);

    // Revoke 1 token — khi logout
    void revokeToken(String token);

    // Revoke tất cả token của user — đổi mật khẩu, admin khóa
    void revokeAllTokensByUser(Long userId);

    // Xóa token hết hạn — chạy định kỳ
    void deleteExpiredTokens();
}
