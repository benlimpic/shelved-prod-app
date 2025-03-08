package com.authentication.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.authentication.demo.Service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserService userService;

  // CONSTRUCTOR
  public SecurityConfig(@Lazy UserService userService) {
    this.userService = userService;
  }

  // LOGIN / LOGOUT ROUTING AND ERROR HANDLING
  private SecurityHandler securityHandler(AuthenticationManager authenticationManager) {
    return new SecurityHandler(userService, authenticationManager);
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return userService;
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
    http.csrf(csrf -> csrf.disable())
        .formLogin(form -> form
            .loginPage("/req/login")
            .successHandler(securityHandler(authenticationManager).loginSuccessHandler())
            .failureHandler(securityHandler(authenticationManager).loginFailureHandler())
            .permitAll())
          .authorizeHttpRequests(auth -> auth
            .requestMatchers("/req/signup", "/req/login", "/css/**", "/js/**").permitAll()
            .requestMatchers("/index", "/user").hasRole("USER")
            .anyRequest().authenticated())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .logoutSuccessHandler(securityHandler(authenticationManager).logoutSuccessHandler()));
    return http.build();
  }
}