package com.authentication.demo.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.UserRepository;
import com.authentication.demo.TestUtils.TestSecurityUtils;
import com.authentication.demo.TestUtils.TestUserFactory;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private S3Service s3Service;

    @Mock
    private ImageService imageService;

    @InjectMocks private UserService userService;

    private UserModel mockUser;
    private final String testBucketName = "shelved-profile-pictures-benlimpic";

@BeforeEach
public void setUp() throws Exception {
    mockUser = TestUserFactory.createMockUser();
    TestSecurityUtils.setAuthenticatedUser(mockUser);

    lenient().when(userRepository.findByUsername(mockUser.getUsername()))
        .thenReturn(Optional.of(mockUser));
    lenient().when(userDetailsService.loadUserByUsername(mockUser.getUsername()))
        .thenReturn(mockUser);

    // Set up manually injected userService with override for getCurrentUser()
    userService = new UserService(userRepository, passwordEncoder, userDetailsService, s3Service, imageService) {
        @Override
        public Optional<UserModel> getCurrentUser() {
            return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication() != null
                ? (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                : null);
        }
    };

    Field bucketField = UserService.class.getDeclaredField("profileImagesBucketName");
    bucketField.setAccessible(true);
    bucketField.set(userService, "test-bucket");
}

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    @Test
    @DisplayName("Should load user details by username when user exists")
    void testLoadUserByUsername_success() {
        UserDetails result = userService.loadUserByUsername("testUser");
        assertEquals("testUser", result.getUsername());
        assertEquals("password", result.getPassword());
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user does not exist")
    void testLoadUserByUsername_failure() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());
        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonExistentUser");
        });
        assertEquals("User not found with username: nonExistentUser", ex.getMessage());
    }

    @Test
    @DisplayName("Should get current user when authenticated")
    void testGetCurrentUser_success() {
        Optional<UserModel> result = userService.getCurrentUser();
        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty optional when no authenticated user")
    void testGetCurrentUser_failure() {
        SecurityContextHolder.clearContext();
        Optional<UserModel> result = userService.getCurrentUser();
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should get current user ID when authenticated")
    void testGetCurrentUserId_success() {
        Long result = userService.getCurrentUserId();
        assertEquals(mockUser.getId(), result);
    }

    @Test
    @DisplayName("Should throw exception when no authenticated user")
    void testGetCurrentUserId_failure() {
        SecurityContextHolder.clearContext();
        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getCurrentUserId());
        assertEquals("No authenticated user found", ex.getMessage());
    }

    @Test
    @DisplayName("Should get user by ID when user exists")
    void testGetUserById_success() {
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        UserModel result = userService.getUserById(mockUser.getId());
        assertEquals(mockUser.getId(), result.getId());
        assertEquals(mockUser.getUsername(), result.getUsername());
    }

    @Test
    @DisplayName("Should throw exception when user does not exist")
    void testGetUserById_failure() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUserById(999L));
        assertEquals("User not found with id: 999", ex.getMessage());
    }

    @Test
    @DisplayName("Should update username when new username is valid")
    void testUpdateUsername_success() {
        String newUsername = "XXXXXXXXXXX";
        when(userRepository.findByUsername(newUsername)).thenReturn(Optional.empty());
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(userDetailsService.loadUserByUsername(newUsername)).thenReturn(mockUser);

        String result = userService.updateUsername(newUsername);
        assertEquals("Username updated successfully", result);
        assertEquals(newUsername, mockUser.getUsername());
    }

    @Test
    @DisplayName("Should not update username when new username is null or empty")
    void testUpdateUsername_failure_emptyUsername() {
        String result = userService.updateUsername(null);
        assertEquals("Username is required", result);
        assertEquals("testUser", mockUser.getUsername());
    }

    @Test
    @DisplayName("Should not update username when new username is the same as the current username")
    void testUpdateUsername_failure_sameUsername() {
        String sameUsername = mockUser.getUsername();
        String result = userService.updateUsername(sameUsername);
        assertEquals("New username is the same as the current username", result);
    }

    @Test
    @DisplayName("Should not update username when new username already exists")
    void testUpdateUsername_failure_usernameTaken() {
        String takenUsername = "existingUser";
        UserModel existingUser = new UserModel();
        existingUser.setUsername(takenUsername);

        when(userRepository.findByUsername(takenUsername)).thenReturn(Optional.of(existingUser));

        String result = userService.updateUsername(takenUsername);
        assertEquals("Username already exists", result);
        assertEquals("testUser", mockUser.getUsername());
    }

    @Test
    @DisplayName("Should not update username when no authenticated user")
    void testUpdateUsername_failure_noAuthenticatedUser() {
        SecurityContextHolder.clearContext();
        String result = userService.updateUsername("newUsername");
        assertEquals("No authenticated user found", result);
    }

    @Test
    @DisplayName("Should update email when new email is valid")
    void testUpdateEmail_success() {
        String newEmail = "newemail@example.com";
        String confirmNewEmail = "newemail@example.com";

        when(userRepository.save(mockUser)).thenReturn(mockUser);

        String result = userService.updateEmail(newEmail, confirmNewEmail);
        assertEquals("Email updated successfully", result);
        assertEquals(newEmail, mockUser.getEmail());
    }

    @Test
    @DisplayName("Should not update email when new email is empty string")
    void testUpdateEmail_failure_emptyString() {
        String originalEmail = mockUser.getEmail();

        String result = userService.updateEmail("", "");

        assertEquals("Email is required", result);
        assertEquals(originalEmail, mockUser.getEmail());

        // Optional: ensure no save occurs
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should not update email when new email is invalid format")
    void testUpdateEmail_failure_invalidFormat() {

        String originalEmail = mockUser.getEmail();
        String invalidEmail = "invalidEmail";

        String result = userService.updateEmail(invalidEmail, invalidEmail);

        assertEquals("Invalid email format", result);
        assertEquals(originalEmail, mockUser.getEmail());

    }

    @Test
    @DisplayName("Should not update email when new email and confirm email do not match")
    void testUpdateEmail_failure_mismatchedEmails() {

        String originalEmail = mockUser.getEmail();
        String newEmail = "newemail@example.com";
        String confirmNewEmail = "differentemail@example.com";

        String result = userService.updateEmail(newEmail, confirmNewEmail);

        assertEquals("Emails do not match", result);
        assertEquals(originalEmail, mockUser.getEmail());

    }

    @Test
    @DisplayName("Should not update email when no authenticated user")
    void testUpdateEmail_failure_noAuthenticatedUser() {
        SecurityContextHolder.clearContext();
        String result = userService.updateEmail("newemail@example.com", "newemail@example.com");
        assertEquals("No authenticated user found", result);

    }

    @Test
    @DisplayName("Should update password when all fields are valid")
    void testUpdatePassword_success() {

        String currentPassword = mockUser.getPassword();
        String newPassword = "XXXXXXXXXXX";
        String confirmNewPassword = "XXXXXXXXXXX";

        lenient().when(passwordEncoder.matches(any(), any())).thenAnswer(invocation -> {
            String raw = invocation.getArgument(0);
            String encoded = invocation.getArgument(1);
            return raw.equals(encoded);
        });
        lenient().when(passwordEncoder.encode(any())).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> {
            UserModel user = invocation.getArgument(0);
            mockUser.setPassword(user.getPassword());
            return mockUser;
        });

        String result = userService.updatePassword(currentPassword, newPassword, confirmNewPassword);

        assertEquals("Password updated successfully", result);
        assertTrue(newPassword.equals(mockUser.getPassword()));

    }

    @Test
    @DisplayName("Should not update password when any field is empty")
    void testUpdatePassword_failure_emptyFields() {

        String originalPassword = mockUser.getPassword();
        String result = userService.updatePassword("", "", "");

        assertEquals("All fields are required", result);
        assertEquals(originalPassword, mockUser.getPassword());

    }

    @Test
    @DisplayName("Should not update password when new passwords do not match")
    void testUpdatePassword_failure_mismatchedPasswords() {

        String originalPassword = mockUser.getPassword();
        String newPassword = "XXXXXXXXXXX";
        String confirmNewPassword = "XXXXXXXXXXY";

        String result = userService.updatePassword(newPassword, newPassword, confirmNewPassword);

        assertEquals("New passwords do not match", result);
        assertEquals(originalPassword, mockUser.getPassword());

    }

    @Test
    @DisplayName("Should not update password when current password is incorrect")
    void testUpdatePassword_failure_incorrectCurrentPassword() {

        String originalPassword = mockUser.getPassword();
        String currentPassword = "XXXXXXXXXXX";
        String newPassword = "XXXXXXXXXXX";
        String confirmNewPassword = "XXXXXXXXXXX";

        String result = userService.updatePassword(currentPassword, newPassword, confirmNewPassword);

        assertEquals("Current password is incorrect", result);
        assertEquals(originalPassword, mockUser.getPassword());

    }

    @Test
    @DisplayName("Should not update password when no authenticated user")
    void testUpdatePassword_failure_noAuthenticatedUser() {
        SecurityContextHolder.clearContext();
        String result = userService.updatePassword("XXXXXXXXXXX", "XXXXXXXXXXX", "XXXXXXXXXXX");
        assertEquals("No authenticated user found", result);
    }

    @Test
    @DisplayName("Should update name when all fields are valid")
    void testUpdateName_success() {

        String firstName = "XXXXXXXXXXX";
        String lastName = "XXXXXXXXXXX";

        // Mock the save method to update the mockUser's fields
        lenient().when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> {
            UserModel user = invocation.getArgument(0);
            mockUser.setFirstName(user.getFirstName());
            mockUser.setLastName(user.getLastName());
            return mockUser;
        });

        String result = userService.updateName(Map.of("firstName", firstName, "lastName", lastName));

        assertEquals("Name updated successfully", result);
        assertEquals(firstName, mockUser.getFirstName());
        assertEquals(lastName, mockUser.getLastName());

    }

    @Test
    @DisplayName("Should not update name when user is not authenticated")
    void testUpdateName_failure_noAuthenticatedUser() {
        SecurityContextHolder.clearContext();
        String result = userService.updateName(Map.of("firstName", "XXXXXXXXXXX", "lastName", "XXXXXXXXXXX"));
        assertEquals("No authenticated user found", result);
    }


    @Test
    @DisplayName("Should save a profile picture and return presigned URL when input is valid")
    void testSaveProfilePicture_success() throws Exception {
        // Use the injected userService and mock S3
        reset(s3Service);

        Field bucketField = UserService.class.getDeclaredField("profileImagesBucketName");
        bucketField.setAccessible(true);
        bucketField.set(userService, "test-bucket");

        // Create and spy on a fake image file
        byte[] imageData = "fake image content".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageData);
        MultipartFile spyFile = spy(mockFile);
        doNothing().when(spyFile).transferTo(any(File.class));

        // Mock expected S3 behavior
        String expectedUrl = "https://mocked-url.com/test.jpg";
        doNothing().when(s3Service).uploadFile(eq("test-bucket"), anyString(), any(InputStream.class), eq("image/jpeg"));
        when(s3Service.generatePresignedUrl(eq("test-bucket"), anyString())).thenReturn(expectedUrl);

        // Act
        String result = userService.saveProfilePicture(spyFile);

        // Assert
        assertEquals(expectedUrl, result);
        verify(s3Service).uploadFile(eq("test-bucket"), anyString(), any(InputStream.class), eq("image/jpeg"));
        verify(s3Service).generatePresignedUrl(eq("test-bucket"), anyString());
    }










    @Test
    @DisplayName("Should update profile and save new profile picture")
    void testUpdateProfile_success() throws Exception {
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setProfilePictureUrl("https://s3.amazonaws.com/test-bucket/old-pic.jpg");

        byte[] imageData = "new image content".getBytes();
        MockMultipartFile uploadedFile = new MockMultipartFile("file", "new.jpg", "image/jpeg", imageData);
        MultipartFile processedFile = spy(uploadedFile);
        doNothing().when(processedFile).transferTo(any(File.class));

        when(imageService.processImage(uploadedFile)).thenReturn(processedFile);
        doNothing().when(s3Service).deleteFile(anyString(), anyString());
        doNothing().when(s3Service).uploadFile(any(), any(), any(), any());
        when(s3Service.generatePresignedUrl(anyString(), anyString()))
        .thenReturn("https://mocked-url.com/new.jpg");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Map<String, String> details = Map.of(
            "location", "New York",
            "website", "https://example.com",
            "biography", "Updated bio"
        );

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        userService.updateProfile(details, uploadedFile, redirectAttributes);

        assertEquals("New York", mockUser.getLocation());
        assertEquals("https://example.com", mockUser.getWebsite());
        assertEquals("Updated bio", mockUser.getBiography());
        assertEquals("https://mocked-url.com/new.jpg", mockUser.getProfilePictureUrl());

        verify(userRepository).save(mockUser);
        verify(redirectAttributes).addFlashAttribute("message", "User profile updated successfully.");
    }











    @Test
    @DisplayName("Should register user successfully when all input is valid")
    void testPostUser_success() {
        // Arrange
        Map<String, String> params = Map.of(
            "username", "newuser",
            "email", "newuser@example.com",
            "password", "securePass123",
            "confirmPassword", "securePass123",
            "firstName", "New",
            "lastName", "User"
        );

        // No user with that username or email
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("securePass123")).thenReturn("encodedPassword");

        // Capture the saved user
        ArgumentCaptor<UserModel> userCaptor = ArgumentCaptor.forClass(UserModel.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Map<String, String> result = userService.postUser(params);

        // Assert
        assertEquals("success", result.get("status"));
        assertEquals("User registered successfully", result.get("message"));

        UserModel savedUser = userCaptor.getValue();
        assertEquals("newuser", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("newuser@example.com", savedUser.getEmail());
        assertEquals("New", savedUser.getFirstName());
        assertEquals("User", savedUser.getLastName());
        assertEquals(List.of("USER"), savedUser.getRoles());

        verify(userRepository).save(savedUser);
    }









    @Test
    @DisplayName("Should return success message when credentials are valid")
    void testLogin_success() {
        // Arrange
        Map<String, String> params = Map.of(
            "username", "testUser",
            "password", "password123"
        );

        // Mock user with matching password
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("password123", mockUser.getPassword())).thenReturn(true);

        // Act
        String result = userService.login(params);

        // Assert
        assertEquals("Login successful", result);
    }

    @Test
    @DisplayName("Logout if valid")
    void testLogout_Success() {
        // Arrange
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(mockUser, null, List.of())
        );
        
        // Act
        userService.logout();
        
        // Assert
        assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);
        verify(userRepository, never()).save(any(UserModel.class)); // Ensure no save operation occurs
    }

    @Test
    @DisplayName("Delete User if valid")
    void testDeleteUser_Success() {
        // Arrange
        when(userRepository.findByUsername(mockUser.getUsername()))
            .thenReturn(Optional.of(mockUser));
        when(userRepository.findByUsername(mockUser.getUsername()))
            .thenReturn(Optional.of(mockUser)); // Called twice in service

        // Act
        String result = userService.deleteUser(mockUser.getUsername());

        // Assert
        verify(s3Service).deleteFile(eq("shelved-profile-pictures-benlimpic"), anyString());
        verify(userRepository).delete(mockUser);
        assertEquals("User deleted successfully", result);
    }

    @Test
    @DisplayName()

}
