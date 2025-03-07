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
import com.authentication.demo.Security.UserLoginConfig;

import com.authentication.demo.Service.UserService;
import com.authentication.demo.logger.AuthenticationLogger;
import com.authentication.demo.Security.UserLogoutConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserService userService;
  private final UserLogoutConfig userLogoutConfig;

  public SecurityConfig(UserService userService, UserLogoutConfig userLogoutConfig) {
    this.userService = userService;
    this.userLogoutConfig = userLogoutConfig;
  }

  private UserLoginConfig formLoginConfig() {
    return new UserLoginConfig();
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
            // LOGIN SUCCCESS HANDLER
            .successHandler(formLoginConfig().customAuthenticationSuccessHandler())
            // LOGIN FAILURE HANDLER
            .failureHandler(formLoginConfig().customAuthenticationFailureHandler())
            .permitAll())

        // AUTHORIZATION
        .authorizeHttpRequests(auth -> auth
            //NO AUTHORIZATION REQUIRED
            .requestMatchers("/req/signup", "/req/login", "/css/**", "/js/**")
            .permitAll()
            // AUTHORIZATION REQUIRED
            .requestMatchers("/index", "/user")
            .authenticated()
            .anyRequest()
            // DENY ALL UNLISTED REQUESTS
            .denyAll()
            )

        // LOGOUT
        .logout(logout -> logout
            .logoutUrl("/logout")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .logoutSuccessUrl("/req/login")
            // LOGOUT HANDLER
            .logoutSuccessHandler(userLogoutConfig.customLogoutSuccessHandler()));
    return http.build();
  }
}