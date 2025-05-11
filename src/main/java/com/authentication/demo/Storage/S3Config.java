
package com.authentication.demo.Storage;
import org.springframework.context.annotation.Bean;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.regions.Regions;
import lombok.extern.slf4j.Slf4j;


import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class S3Config {

  @Bean
  public AmazonS3 s3() {
    return AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_1).build();
  }
}
