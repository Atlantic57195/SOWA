package com.berkdagli.sowa.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, UserBucket> buckets = new ConcurrentHashMap<>();

    // allow 1000 requests per minute
    private static final long CAPACITY = 101;
    private static final long REFILL_DURATION_SECONDS = 60; // 1 minute

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        UserBucket bucket = buckets.computeIfAbsent(ip, k -> new UserBucket());

        if (bucket.tryConsume()) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("Too many requests");
        }
    }

    private static class UserBucket {
        private long tokens;
        private Instant lastRefill;

        public UserBucket() {
            this.tokens = CAPACITY;
            this.lastRefill = Instant.now();
        }

        public synchronized boolean tryConsume() {
            refill();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        private void refill() {
            Instant now = Instant.now();
            long secondsElapsed = Duration.between(lastRefill, now).getSeconds();

            // Refill logic: refill all at once after duration or incrementally?
            // Simple approach: if duration passed, reset.
            // Better approach for smooth rate limiting: add tokens proportional to time.
            // Requirement says "1000 requests per minute".
            // Let's stick to a simple refill: calculate how many tokens to add.
            // Rate = CAPACITY / REFILL_DURATION_SECONDS tokens per second.
            // But 1000/60 is 16.66.. so let's use double token bucket or just simple reset
            // if we want "per minute".
            // The example "1000 passwords per minute" usually implies a window.
            // Let's implement a standard token bucket where we add tokens based on elapsed
            // time.

            if (secondsElapsed > 0) {
                // Calculate tokens to add: (capacity / duration) * elapsed
                // To avoid precision issues with integer math for small elapsed times,
                // we can just check if enough time passed for at least 1 token if rate is slow,
                // or just use float.
                // Actually, simplest valid implementation for "1000 per minute":
                // refill rate = 1000 tokens / 60 seconds ~= 16 tokens/sec.

                long tokensToAdd = (secondsElapsed * CAPACITY) / REFILL_DURATION_SECONDS;
                if (tokensToAdd > 0) {
                    tokens = Math.min(CAPACITY, tokens + tokensToAdd);
                    lastRefill = now;
                }
            }
        }
    }
}
