package com.authentication.demo;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.authentication.demo.Service.S3Service;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan(basePackages = "com.authentication.demo")
public class AuthenticationDemoApplication {

	private final S3Service s3Service;

	public AuthenticationDemoApplication(S3Service s3Service) {
		this.s3Service = s3Service;
	}

	@PostConstruct
	public void initializeS3Operations() {

    String activeProfile = System.getProperty("spring.profiles.active", "default");
    if ("test".equals(activeProfile)) {
        System.out.println("Skipping S3 operations in test profile");
        return;
    }

		String bucketName = "shelved-bucket";
		this.s3Service.createBucket(bucketName);
		String filePath = "/Users/benlimpic/Desktop/OB.png";
		String fileName = "OB.png";
		String downloadPath = "/download/to/files/downloaded-file.png";

		// UPLOAD S3
		s3Service.uploadFile(bucketName, fileName, filePath);
		System.out.println("File uploaded successfully");

		// DOWNLOAD S3
		s3Service.downloadFile(bucketName, fileName, downloadPath);
		System.out.println("File downloaded successfully");

		// LIST FILES S3
		List<String> files = s3Service.listFiles(bucketName);
		for (String file : files) {
			System.out.println(file);
		}

		// LIST BUCKETS S3
		this.s3Service.createBucket("shelved-bucket-vis-2");
		s3Service.listS3Buckets().forEach(System.out::println);

		// REMOVE BUCKETS S3
		s3Service.removeS3Bucket(bucketName);
		s3Service.removeS3Bucket("shelved-bucket-vis-2");
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationDemoApplication.class, args);
	}
}
