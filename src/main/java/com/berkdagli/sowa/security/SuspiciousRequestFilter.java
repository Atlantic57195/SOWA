package com.berkdagli.sowa.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class SuspiciousRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SuspiciousRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Simple heuristic for suspicious requests (e.g., potential SQL injection or
        // XSS patterns in params)
        // NOTE: This is basic. For production, use a WAF or more robust validation.

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] values = request.getParameterValues(paramName);

            if (values != null) {
                for (String value : values) {
                    if (isSuspicious(value)) {
                        String remoteAddr = request.getRemoteAddr();
                        String uri = request.getRequestURI();
                        logger.warn("Suspicious input detected. IP: '{}', URI: '{}', Param: '{}', Value: '{}'",
                                remoteAddr, uri, paramName, value);
                        // Depending on policy, you might want to block the request here.
                        // For now, we just log it.
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isSuspicious(String value) {
        if (value == null)
            return false;
        // Basic checks for common attack vectors
        String lowerValue = value.toLowerCase();
        return lowerValue.contains("<script>") ||
                lowerValue.contains("union select") ||
                lowerValue.contains("drop table") ||
                lowerValue.contains("exec(");
    }
}
