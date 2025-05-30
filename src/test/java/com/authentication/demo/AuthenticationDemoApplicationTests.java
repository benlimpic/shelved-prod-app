package com.authentication.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.authentication.demo.Service.S3Service;

@SpringBootTest
public class AuthenticationDemoApplicationTests {

    @MockBean
    private S3Service s3Service;

    @Test
    void contextLoads() {
        // Test context loading without performing actual S3 operations
    }
}
