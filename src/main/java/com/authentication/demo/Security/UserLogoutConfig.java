package com.authentication.demo.Security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.authentication.demo.logger.AuthenticationLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class UserLogoutConfig {

  @Bean
  public LogoutSuccessHandler customLogoutSuccessHandler() {
    return new LogoutSuccessHandler() {

      @Override
      public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
          Authentication authentication) throws IOException, ServletException {
        // Logout success handling
        AuthenticationLogger
            .log("Logout successful: " + (authentication != null ? authentication.getName() : "Anonymous"));
            
        response.sendRedirect("/req/login");
      }
    };
  }

}
