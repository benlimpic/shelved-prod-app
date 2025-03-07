package com.authentication.demo.Security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.authentication.demo.Service.UserService;
import com.authentication.demo.logger.AuthenticationLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserService userService;

  public SecurityConfig(UserService userService) {
    this.userService = userService;
  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user = User.withUsername("user")
        .password(passwordEncoder().encode("password"))
        .roles("USER")
        .build();
    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        // LOGIN FORM
        .formLogin(form -> form
            .loginPage("/req/login")
            .loginProcessingUrl("/req/login")
            .permitAll()
            .defaultSuccessUrl("/index")
            .failureUrl("/req/login?error=true")

            // LOGIN SUCCESS HANDLER
            .successHandler((request, response, authentication) -> {
              request.getSession().setAttribute("username", authentication.getName());
              AuthenticationLogger.log("User " + authentication.getName() + " logged in successfully.");
              response.sendRedirect("/index");
            })

            // LOGIN FAILURE HANDLER
            .failureHandler(customAuthenticationFailureHandler()))

        // AUTHORIZATION
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/req/signup", "/req/login", "/css/**", "/js/**")
            .permitAll()
            .anyRequest()
            .authenticated())

        // LOGOUT
        .logout(logout -> logout
            .logoutUrl("/logout")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .logoutSuccessUrl("/req/login")
            // LOGOUT HANDLER
            .addLogoutHandler((request, response, authentication) -> {
              if (authentication != null) {
                AuthenticationLogger.log("User " + authentication.getName() + " logged out successfully.");
              }
            }));
    return http.build();
  }

  @Bean
  public AuthenticationFailureHandler customAuthenticationFailureHandler() {
    return new AuthenticationFailureHandler() {
      @Override
      // LOGIN FAILURE ERROR HANDLING
      public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
          AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof BadCredentialsException) {
          request.getSession().setAttribute("error", "Invalid username or password.");
        } else if (exception instanceof DisabledException) {
          request.getSession().setAttribute("error", "User account is disabled.");
        } else {
          request.getSession().setAttribute("error", "Authentication failed: " + exception.getMessage());
        }
        AuthenticationLogger.log("Login failed: " + exception.getMessage());
        response.sendRedirect("/req/login?error=true");
      }
    };
  }
}