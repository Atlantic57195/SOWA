package com.berkdagli.sowa.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : "Anonymous";
        String uri = request.getRequestURI();
        String remoteAddr = request.getRemoteAddr();

        // SECURE LOGGING: Log 403 Forbidden attempts
        logger.warn("Unauthorized access attempt (403). User: '{}', Resource: '{}', IP: '{}'",
                user, uri, remoteAddr);

        response.sendRedirect(request.getContextPath() + "/access-denied");
    }
}
