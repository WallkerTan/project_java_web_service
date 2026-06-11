package com.example.hospital.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hospital.model.entity.TokenBlacklist;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

    // Kiểm tra token có trong blacklist không
    boolean existsByToken(String token);

    // Kiểm tra user có token bị thu hồi không
    boolean existsByUserId(Long userId);

    // Xóa toàn bộ token của user (revokeToken)
    void deleteAllByUserId(Long userId);

    // Xóa token đã hết hạn (chạy định kỳ)
    void deleteAllByExpiredAtBefore(LocalDateTime now);

}
