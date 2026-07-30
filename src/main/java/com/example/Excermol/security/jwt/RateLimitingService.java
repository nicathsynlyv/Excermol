package com.example.Excermol.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class RateLimitingService {
    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_DURATION_MINUTES = 15;

    private static final String EMAIL_PREFIX = "login_attempts:email:";
    private static final String IP_PREFIX = "login_attempts:ip:";

    private final RedisTemplate<String, String> redisTemplate;

    public RateLimitingService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Login cəhdindən əvvəl çağırılır - blokdadırmı yoxlayır
    public void checkRateLimit(String email, String ip) {
        checkKey(EMAIL_PREFIX + email, "Bu email üçün çox sayda uğursuz cəhd oldu");
        checkKey(IP_PREFIX + ip, "Bu IP ünvanından çox sayda uğursuz cəhd oldu");
    }

    private void checkKey(String key, String message) {
        String value = redisTemplate.opsForValue().get(key);
        if (value != null && Integer.parseInt(value) >= MAX_ATTEMPTS) {
            log.warn("Rate limit exceeded for key: {}", key);
            throw new RateLimitExceededException(message + ". " + BLOCK_DURATION_MINUTES + " dəqiqə sonra yenidən cəhd edin");
        }
    }

    // Uğursuz login-dən sonra çağırılır - sayğacı artırır
    public void recordFailedAttempt(String email, String ip) {
        incrementKey(EMAIL_PREFIX + email);
        incrementKey(IP_PREFIX + ip);
    }

    private void incrementKey(String key) {
        Long attempts = redisTemplate.opsForValue().increment(key);
        if (attempts != null && attempts == 1) {
            // İlk cəhddirsə, expiry təyin et
            redisTemplate.expire(key, Duration.ofMinutes(BLOCK_DURATION_MINUTES));
        }
        log.info("Failed login attempt recorded for key: {} (attempt #{})", key, attempts);
    }

    // Uğurlu login-dən sonra çağırılır - sayğacları sıfırlayır
    public void resetAttempts(String email, String ip) {
        redisTemplate.delete(EMAIL_PREFIX + email);
        redisTemplate.delete(IP_PREFIX + ip);
    }
}
