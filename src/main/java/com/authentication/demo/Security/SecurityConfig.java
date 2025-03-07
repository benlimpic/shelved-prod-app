package com.authentication.demo.Security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.authentication.demo.Service.UserService;
import com.authentication.demo.logger.AuthenticationLogger;

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
        .formLogin(form -> form
            .loginPage("/req/login")
            .loginProcessingUrl("/req/login")
            .permitAll()
            .defaultSuccessUrl("/index")
            .failureUrl("/req/login?error=true")
            //LOGIN SUCCESS HANDLER
            .successHandler((request, response, authentication) -> {
              request.getSession().setAttribute("username", authentication.getName());
              AuthenticationLogger.log("User " + authentication.getName() + " logged in successfully.");
              response.sendRedirect("/index");
            })
            //LOGIN FAILURE HANDLER
            .failureHandler((request, response, exception) -> {
              request.getSession().setAttribute("error", exception.getMessage());
              response.sendRedirect("/req/login?error=true");
              AuthenticationLogger.log("Login failed: " + exception.getMessage());
                }))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/req/signup", "/req/login", "/css/**", "/js/**")
            .permitAll()
            .anyRequest()
            .authenticated())
        .logout(logout -> logout
            //LOGOUT SUCCESS HANDLER
            .addLogoutHandler((request, response, authentication) -> {
              if (authentication != null) {
                AuthenticationLogger.log("User " + authentication.getName() + " logged out successfully.");
                request.getSession().invalidate();
              }
              try {
                response.sendRedirect("/req/login");
              } catch (IOException e) {
                e.printStackTrace();
              }
            })
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            );
    return http.build();
  }
}