package com.authentication.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.authentication.demo.logger.AuthenticationLogger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityHandler {


    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            // LOGIN SUCCESS HANDLING
            AuthenticationLogger.log("Login successful: " + authentication.getName());
            AuthenticationLogger.logAuthenticationDetails();
            response.sendRedirect("/index");
        };
    }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return (HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) -> {
            // LOGIN FAILURE ERROR HANDLING
            if (exception instanceof BadCredentialsException) {
                request.getSession().setAttribute("error", "Invalid username or password.");
            }
            AuthenticationLogger.log("Login failed");
            AuthenticationLogger.logAuthenticationDetails();
            response.sendRedirect("/login?error=true");
        };
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            // LOGOUT SUCCESS HANDLING
            AuthenticationLogger.log("Logout successful");
            AuthenticationLogger.logAuthenticationDetails();
            response.sendRedirect("/login?logout=true");
        };
    }
}