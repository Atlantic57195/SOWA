package com.berkdagli.sowa.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        String uri = request.getRequestURI();
        String remoteAddr = request.getRemoteAddr();

        // SECURE LOGGING: Log 401 Unauthorized attempts (unauthenticated access to
        // protected resource)
        logger.warn("Unauthenticated access attempt (401). Resource: '{}', IP: '{}', Msg: {}",
                uri, remoteAddr, authException.getMessage());

        response.sendRedirect(request.getContextPath() + "/login");
    }
}
