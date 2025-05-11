package com.authentication.demo.Storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Configuration
public class S3Config {

  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
                   .region(Region.US_WEST_1) // Replace with your desired region
                   .build();
  }
}