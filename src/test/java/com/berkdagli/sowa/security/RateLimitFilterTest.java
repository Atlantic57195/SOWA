package com.berkdagli.sowa.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateLimitFilterTest {

    @Test
    void testRateLimit() throws ServletException, IOException {
        RateLimitFilter filter = new RateLimitFilter();
        // Simulate 1000 requests from same IP (within capacity)
        for (int i = 0; i < 1000; i++) {
            MockFilterChain filterChain = new MockFilterChain();
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRemoteAddr("127.0.0.1");
            MockHttpServletResponse response = new MockHttpServletResponse();

            filter.doFilter(request, response, filterChain);

            assertEquals(200, response.getStatus(), "Request " + i + " should succeed");
        }

        // The 1001st request should fail
        MockFilterChain filterChain = new MockFilterChain();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        assertEquals(429, response.getStatus(), "Request 1001 should be rate limited");
    }

    @Test
    void testDifferentIps() throws ServletException, IOException {
        RateLimitFilter filter = new RateLimitFilter();

        // 1000 requests from IP 1
        for (int i = 0; i < 1000; i++) {
            MockFilterChain filterChain = new MockFilterChain();
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRemoteAddr("192.168.1.1");
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request, response, filterChain);
        }

        // 1 request from IP 2 (should succeed even if IP 1 is exhausted)
        MockFilterChain filterChain = new MockFilterChain();
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        request2.setRemoteAddr("192.168.1.2");
        MockHttpServletResponse response2 = new MockHttpServletResponse();

        filter.doFilter(request2, response2, filterChain);

        assertEquals(200, response2.getStatus(), "Request from different IP should succeed");
    }
}
