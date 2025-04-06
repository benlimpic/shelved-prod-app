package com.authentication.demo.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
  
  // PROFILE IMAGES
      registry.addResourceHandler("/profile-pictures/**")
              .addResourceLocations("file:./profile-pictures/");

  //COLLECTION IMAGES
      registry.addResourceHandler("/collection-images/**")
              .addResourceLocations("file:collection-images/");

  //IMAGES
      registry.addResourceHandler("/images/**")
              .addResourceLocations("classpath:/static/images/");
  }
}