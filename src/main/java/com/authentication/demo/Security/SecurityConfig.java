package com.authentication.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
    .formLogin(httpForm -> {
      httpForm
      .loginPage("/login")
      .loginProcessingUrl("/login")
      .defaultSuccessUrl("/home", true)
      .failureUrl("/login?error=true")
      .permitAll();
    })

    .authorizeHttpRequests(registry -> {
      registry.requestMatchers("/req/signup").permitAll();
      registry.anyRequest().authenticated();
    })
    .build();
  }

}
