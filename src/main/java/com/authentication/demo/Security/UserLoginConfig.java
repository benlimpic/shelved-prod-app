package com.authentication.demo.Security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.authentication.demo.logger.AuthenticationLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class UserLoginConfig {

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                // LOGIN SUCCESS HANDLING
                AuthenticationLogger.log("Login successful: " + authentication.getName());
                AuthenticationLogger.logAuthenticationDetails();
                response.sendRedirect("/index");
            }
        };
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                    AuthenticationException exception) throws IOException, ServletException {
                // LOGIN FAILURE ERROR HANDLING
                if (exception instanceof BadCredentialsException) {
                    request.getSession().setAttribute("error", "Invalid username or password.");
                } else if (exception instanceof DisabledException) {
                    request.getSession().setAttribute("error", "User account is disabled.");
                } else {
                    request.getSession().setAttribute("error", "Authentication failed: " + exception.getMessage());
                }
                AuthenticationLogger.log("Login failed: " + exception.getMessage());
                AuthenticationLogger.logAuthenticationDetails();
                response.sendRedirect("/req/login?error=true");
            }
        };
    }
}