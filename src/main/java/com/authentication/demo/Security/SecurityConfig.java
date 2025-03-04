package com.authentication.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.authentication.demo.Service.MyAppUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final MyAppUserService appUserService;

  public SecurityConfig(MyAppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return appUserService;
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(appUserService);
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
              .permitAll()
              .defaultSuccessUrl("/index")
              .failureUrl("/req/login?error=true"))
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/req/signup", "/css/**", "/js/**")
              .permitAll()
              .anyRequest()
              .authenticated())
          .logout(logout -> logout
              .logoutUrl("/logout")
              .invalidateHttpSession(true)
              .clearAuthentication(true)
              .deleteCookies("JSESSIONID")
              .logoutSuccessUrl("/req/login"));
      return http.build();
  }

}
