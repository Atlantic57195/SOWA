package com.berkdagli.sowa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final com.berkdagli.sowa.security.CustomAuthenticationFailureHandler authenticationFailureHandler;
        private final com.berkdagli.sowa.security.CustomAccessDeniedHandler accessDeniedHandler;
        private final com.berkdagli.sowa.security.CustomAuthenticationEntryPoint authenticationEntryPoint;
        private final com.berkdagli.sowa.security.SuspiciousRequestFilter suspiciousRequestFilter;
        private final com.berkdagli.sowa.security.RateLimitFilter rateLimitFilter;

        public SecurityConfig(
                        com.berkdagli.sowa.security.CustomAuthenticationFailureHandler authenticationFailureHandler,
                        com.berkdagli.sowa.security.CustomAccessDeniedHandler accessDeniedHandler,
                        com.berkdagli.sowa.security.CustomAuthenticationEntryPoint authenticationEntryPoint,
                        com.berkdagli.sowa.security.SuspiciousRequestFilter suspiciousRequestFilter,
                        com.berkdagli.sowa.security.RateLimitFilter rateLimitFilter) {
                this.authenticationFailureHandler = authenticationFailureHandler;
                this.accessDeniedHandler = accessDeniedHandler;
                this.authenticationEntryPoint = authenticationEntryPoint;
                this.suspiciousRequestFilter = suspiciousRequestFilter;
                this.rateLimitFilter = rateLimitFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .addFilterBefore(rateLimitFilter,
                                                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(suspiciousRequestFilter,
                                                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/login", "/register", "/api/register", "/api/login",
                                                                "/css/**", "/js/**", "/favicon.ico",
                                                                "/.well-known/appspecific/com.chrome.devtools.json")
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/user/**").hasRole("USER")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .usernameParameter("email") // Use email as username
                                                .defaultSuccessUrl("/hello", true) // Redirect to hello page on success
                                                .failureHandler(authenticationFailureHandler) // Use custom failure
                                                                                              // handler
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                .exceptionHandling(e -> e
                                                .accessDeniedHandler(accessDeniedHandler) // Use custom access denied
                                                                                          // handler
                                                .authenticationEntryPoint(authenticationEntryPoint)) // Use custom entry
                                                                                                     // point
                                .headers(headers -> headers
                                                .contentSecurityPolicy(csp -> csp.policyDirectives(
                                                                "default-src 'self'; script-src 'self'; object-src 'none'; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; font-src 'self' https://fonts.gstatic.com;"))
                                                .referrerPolicy(referrer -> referrer.policy(
                                                                org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                                                .httpStrictTransportSecurity(hsts -> hsts // Bonus: HSTS
                                                                .includeSubDomains(true)
                                                                .preload(true)
                                                                .maxAgeInSeconds(31536000)));

                return http.build();
        }

        @Bean
        public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
                return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        }
}
