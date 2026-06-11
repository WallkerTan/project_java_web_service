package com.example.hospital.service;

import com.example.hospital.model.dto.request.LoginRequest;
import com.example.hospital.model.dto.request.UserRequest;
import com.example.hospital.model.dto.respon.UserRespon;

public interface AuthService {

    // Đăng nhập — trả token + info
    UserRespon login(LoginRequest request);

    // Đăng ký — trả info user vừa tạo
    UserRespon register(UserRequest request);

    // Xoay vòng token — trả accessToken mới
    UserRespon refreshToken(String refreshToken);

    // Đăng xuất — đưa token vào blacklist
    void logout(String token);

    // Kiểm tra user có đang đăng nhập không
    boolean isLoggedIn(Long userId);

    // Thu hồi token — Admin khóa tài khoản
    void revokeToken(Long userId);
}
