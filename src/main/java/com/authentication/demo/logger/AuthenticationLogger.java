package com.authentication.demo.logger;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationLogger {

    public static void logAuthenticationDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("Authentication Details:");
            System.out.println("  Name: " + authentication.getName());
            System.out.println("  Principal: " + authentication.getPrincipal());
            System.out.println("  Credentials: " + authentication.getCredentials());
            System.out.println("  Authorities: " + authentication.getAuthorities());
        } else {
            System.out.println("No authentication information available.");
        }
    }

    public static void log(String message) {
        System.out.println(message);
    }
}