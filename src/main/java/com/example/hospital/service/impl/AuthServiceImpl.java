package com.example.hospital.service.impl;

import java.time.ZoneId;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.hospital.mapper.UserMapper;
import com.example.hospital.model.dto.request.LoginRequest;
import com.example.hospital.model.dto.request.UserRequest;
import com.example.hospital.model.dto.respon.UserRespon;
import com.example.hospital.model.entity.TokenBlacklist;
import com.example.hospital.model.entity.User;
import com.example.hospital.model.enums.UserRole;
import com.example.hospital.model.enums.UserStatus;
import com.example.hospital.repository.TokenBlacklistRepository;
import com.example.hospital.repository.UserRepository;
import com.example.hospital.security.JwtUtil;
import com.example.hospital.service.AuthService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    public UserRespon login(LoginRequest request) {
        // 1. Xác thực — sai password tự ném BadCredentialsException → 401
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUserName(), request.getPassword()));

        // 2. Load user
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Sinh token + trả về
        return UserRespon.builder().id(user.getId()).userName(user.getUsername())
                .email(user.getEmail()).role(user.getRole())
                .accessToken(jwtUtil.generateAccessToken(user))
                .refreshToken(jwtUtil.generateRefreshToken(user)).build();
    }

    @Override
    public UserRespon register(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = userRepository.findByUserName(request.getUserName()).orElse(null);
        user = userMapper.toEntity(request, user);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.PATIENT);
        user.setStatus(UserStatus.ACTIVE);

        return userMapper.toRespon(userRepository.save(user));
    }

    @Override
    public UserRespon refreshToken(String refreshToken) {
        // 1. Validate refresh token
        String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtUtil.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Refresh token invalid or expired");
        }

        // 2. Sinh accessToken mới
        return UserRespon.builder().userName(user.getUsername())
                .accessToken(jwtUtil.generateAccessToken(user)).refreshToken(refreshToken) // giữ
                                                                                           // nguyên
                                                                                           // refreshToken
                                                                                           // cũ
                .build();
    }

    @Override
    public void logout(String token) {
        // Lưu token vào blacklist
        TokenBlacklist blacklist =
                TokenBlacklist
                        .builder().token(token).expiredAt(jwtUtil.extractExpiration(token)
                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                        .build();
        tokenBlacklistRepository.save(blacklist);
    }

    @Override
    public boolean isLoggedIn(Long userId) {
        // Kiểm tra user có token hợp lệ không trong blacklist
        return !tokenBlacklistRepository.existsByUserId(userId);
    }

    @Override
    public void revokeToken(Long userId) {
        // Admin thu hồi — xóa toàn bộ token của user
        tokenBlacklistRepository.deleteAllByUserId(userId);
        // hoặc đánh dấu user bị LOCKED
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(UserStatus.lOCKED);
        userRepository.save(user);
    }
}
