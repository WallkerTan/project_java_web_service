package com.example.hospital.scheduler;

import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.hospital.repository.TokenBlacklistRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Scheduled(cron = "0 0 2 * * *") // chạy lúc 2 giờ sáng mỗi ngày
    public void cleanExpiredTokens() {
        tokenBlacklistRepository.deleteAllByExpiredAtBefore(LocalDateTime.now());
    }
}
