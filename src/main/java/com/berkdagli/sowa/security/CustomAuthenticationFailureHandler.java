package com.berkdagli.sowa.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    public CustomAuthenticationFailureHandler() {
        setDefaultFailureUrl("/login?error");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("email"); // Assuming email is used as username as per SecurityConfig
        String remoteAddr = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        // SECURE LOGGING: Log failing username and IP, but NEVER the password!
        logger.warn("Failed login attempt. Username: '{}', IP: '{}', User-Agent: '{}', Reason: {}",
                username, remoteAddr, userAgent, exception.getMessage());

        super.onAuthenticationFailure(request, response, exception);
    }
}
