package inu.helloforeigner.common.security.handler;

import java.util.concurrent.TimeUnit;
import javax.security.auth.login.AccountLockedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {
    private final RedisTemplate<String, Integer> redisTemplate;
    private static final int MAX_ATTEMPTS = 5;

    public void incrementAttempts(String email) throws AccountLockedException {
        String key = "login:" + email;
        Integer attempts = redisTemplate.opsForValue().get(key);
        if (attempts == null) {
            redisTemplate.opsForValue().set(key, 1, 1, TimeUnit.HOURS);
        } else if (attempts < MAX_ATTEMPTS) {
            redisTemplate.opsForValue().increment(key);
        } else {
            throw new AccountLockedException("계정이 잠겼습니다. 1시간 후에 다시 시도해주세요.");
        }
    }
}