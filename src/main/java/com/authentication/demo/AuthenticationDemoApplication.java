package com.authentication.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.authentication.demo")
public class AuthenticationDemoApplication {

	// @Autowired
	// private UserService userService;

	// private final S3Service s3Service;

	// public AuthenticationDemoApplication(S3Service s3Service) {
	// 	this.s3Service = s3Service;
	// }

	// @PostConstruct
	// public void initializeS3Operations() {
	// 	String activeProfile = System.getProperty("spring.profiles.active", "default");
	// 	if ("test".equals(activeProfile)) {
	// 		System.out.println("Skipping S3 operations in test profile");
	// 		return;
	// 	}

	// 	String bucketName = "shelved-bucket";
	// 	this.s3Service.createBucket(bucketName);
	// 	String filePath = "/Users/benlimpic/Desktop/OB.png";
	// 	String fileName = "Obama.png";
	// 	String downloadPath = System.getProperty("user.home") + "/Downloads/test/check/downloaded-Obama.png";

	// 	// UPLOAD S3
	// 	s3Service.uploadFile(bucketName, fileName, filePath);
	// 	System.out.println("File uploaded successfully");

	// 	// DOWNLOAD S3
	// 	s3Service.downloadFile(bucketName, fileName, downloadPath);
	// 	System.out.println("File downloaded successfully");

	// 	// LIST FILES S3
	// 	List<String> files = s3Service.listFiles(bucketName);
	// 	for (String file : files) {
	// 		System.out.println(file);
	// 	}

	// 	// LIST BUCKETS S3
	// 	this.s3Service.createBucket("shelved-bucket-vis-2");
	// 	s3Service.listS3Buckets().forEach(System.out::println);

	// 	// REMOVE BUCKETS S3
	// 	// s3Service.removeS3Bucket(bucketName);
	// 	// s3Service.removeS3Bucket("shelved-bucket-vis-2");

	// 	// Test uploading a profile photo
	// 	testUploadProfilePhoto();
	// }

	// private void testUploadProfilePhoto() {
	// 	try {
	// 		System.out.println("Starting profile photo upload test...");

	// 		// Load the test file
	// 		File testFile = new File("/Users/benlimpic/Desktop/test-profile-photo.png");
	// 		if (!testFile.exists()) {
	// 			System.out.println("Test file does not exist: " + testFile.getAbsolutePath());
	// 			return;
	// 		}

	// 		// Convert the file to a MultipartFile
	// 		MultipartFile multipartFile = convertFileToMultipartFile(testFile);

	// 		// Use UserService to upload the profile photo
	// 		// Use the existing userService field
	// 		String fileUrl = userService.saveProfilePicture(multipartFile);
	// 		if (fileUrl == null) {
	// 			System.out.println("Failed to upload profile photo.");
	// 			return;
	// 		}

	// 		System.out.println("Profile photo uploaded successfully. File URL: " + fileUrl);
	// 	} catch (Exception e) {
	// 		System.err.println("Error during profile photo upload test: " + e.getMessage());
	// 		e.printStackTrace();
	// 	}
	// }

	// private MultipartFile convertFileToMultipartFile(File file) throws IOException {
	// 	return new MockMultipartFile(
	// 			"profilePicture", // Parameter name
	// 			file.getName(), // Original filename
	// 			"image/png", // Content type
	// 			new FileInputStream(file) // File content
	// 	);
	// }

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationDemoApplication.class, args);
	}
}
