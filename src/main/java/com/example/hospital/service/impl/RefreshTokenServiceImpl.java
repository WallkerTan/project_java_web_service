package com.example.hospital.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.hospital.model.entity.RefreshToken;
import com.example.hospital.model.entity.User;
import com.example.hospital.repository.RefreshTokenRepository;
import com.example.hospital.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration; // milliseconds

    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder().token(UUID.randomUUID().toString()) // random
                                                                                               // chuỗi
                                                                                               // unique
                .user(user).expiryDate(Instant.now().plusMillis(refreshExpiration)).revoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken createRefreshToken2(User user, String token) {
        RefreshToken refreshToken = RefreshToken.builder().token(token) // random
                                                                        // chuỗi
                                                                        // unique
                .user(user).expiryDate(Instant.now().plusMillis(refreshExpiration)).revoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = findByToken(token);

        // Kiểm tra bị revoke chưa
        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token đã bị thu hồi");
        }

        // Kiểm tra hết hạn chưa
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token đã hết hạn");
        }

        return refreshToken;
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token không tồn tại"));
    }

    @Override
    public void revokeToken(String token) {
        RefreshToken refreshToken = findByToken(token);
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void revokeAllTokensByUser(Long userId) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserId(userId);
        tokens.forEach(t -> t.setRevoked(true));
        refreshTokenRepository.saveAll(tokens);
    }

    @Override
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteAllByExpiryDateBefore(Instant.now());
    }

    @Override
    public List<RefreshToken> findAllByUserId(Long userId) {
        return refreshTokenRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteAllByExpiryDateBefore(Instant now) {
        refreshTokenRepository.deleteAllByExpiryDateBefore(now);
    }
}
