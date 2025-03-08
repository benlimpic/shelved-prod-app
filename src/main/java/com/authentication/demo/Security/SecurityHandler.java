package com.authentication.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.authentication.demo.Service.UserService;
import com.authentication.demo.logger.AuthenticationLogger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityHandler {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public SecurityHandler(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            // LOGIN SUCCESS HANDLING
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);

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
            response.sendRedirect("/req/login?error=true");
        };
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            // LOGOUT SUCCESS HANDLING
            AuthenticationLogger.log("Logout successful");
            AuthenticationLogger.logAuthenticationDetails();
            response.sendRedirect("/req/login");
        };
    }
}