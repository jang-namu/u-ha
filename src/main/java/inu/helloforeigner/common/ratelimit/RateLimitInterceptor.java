package inu.helloforeigner.common.ratelimit;

import inu.helloforeigner.common.security.exception.SecurityExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// common/security/ratelimit/RateLimitInterceptor.java
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RedisTemplate<String, Integer> redisTemplate;
    private final int MAX_REQUESTS = 100; // 제한 횟수
    private final int TIME_WINDOW = 60; // 시간 윈도우(초)

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String ip = getClientIP(request);
        String uri = request.getRequestURI();
        String key = "rate:" + ip + ":" + uri;

        Integer count = redisTemplate.opsForValue().get(key);
        if (count == null) {
            redisTemplate.opsForValue().set(key, 1, TIME_WINDOW, TimeUnit.SECONDS);
        } else if (count >= MAX_REQUESTS) {
            throw new TooManyRequestsException(SecurityExceptionCode.ACCOUNT_LOCKED);
        } else {
            redisTemplate.opsForValue().increment(key);
        }

        return true;
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static class TooManyRequestsException extends RuntimeException {
        private final SecurityExceptionCode errorCode;

        public TooManyRequestsException(SecurityExceptionCode errorCode) {
            super(errorCode.getMessage());
            this.errorCode = errorCode;
        }

        public TooManyRequestsException(SecurityExceptionCode errorCode, String message) {
            super(message);
            this.errorCode = errorCode;
        }
    }
}
