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

    // allow 100 requests per minute
    private static final long CAPACITY = 100;
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
            if (secondsElapsed > 0) {
                long tokensToAdd = (secondsElapsed * CAPACITY) / REFILL_DURATION_SECONDS;
                if (tokensToAdd > 0) {
                    tokens = Math.min(CAPACITY, tokens + tokensToAdd);
                    lastRefill = now;
                }
            }
        }
    }
}
