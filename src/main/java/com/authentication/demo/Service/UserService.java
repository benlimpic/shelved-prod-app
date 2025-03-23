package com.authentication.demo.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;

  public UserService(
      UserRepository repository,
      PasswordEncoder passwordEncoder,
      @Lazy UserDetailsService userDetailsService) {
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserModel> user = repository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    UserModel userModel = user.get();
    List<GrantedAuthority> authorities = userModel.getRoles().stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    return new org.springframework.security.core.userdetails.User(
        userModel.getUsername(),
        userModel.getPassword(),
        authorities);
  }

  // GET CURRENT USER
  private Optional<UserModel> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      return repository.findByUsername(userDetails.getUsername());
    }
    return Optional.empty();
  }

  // UPDATE USERNAME
  public String updateUsername(String newUsername) {
    if (newUsername == null || newUsername.isEmpty()) {
      return "Username is required";
    }

    Optional<UserModel> existingUser = repository.findByUsername(newUsername);
    if (existingUser.isPresent()) {
      return "Username already exists";
    }

    Optional<UserModel> user = getCurrentUser();
    if (user.isPresent()) {
      UserModel userModel = user.get();
      userModel.setUsername(newUsername);
      repository.save(userModel);

      // Re-authenticate the user with the new username
      UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(newUsername);
      Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserDetails, null,
          updatedUserDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(newAuth);

      return "Username updated successfully";
    } else {
      return "No authenticated user found";
    }
  }

  // UPDATE EMAIL
  public String updateEmail(String newEmail, String confirmNewEmail) {

    if (newEmail == null || newEmail.isEmpty() ||
        confirmNewEmail == null || confirmNewEmail.isEmpty()) {
      return "Emails are required";
    }

    if (!newEmail.equals(confirmNewEmail)) {
      return "New emails do not match";
    }

    Optional<UserModel> user = getCurrentUser();
    if (user.isPresent()) {
      UserModel userModel = user.get();
      userModel.setEmail(newEmail);
      repository.save(userModel);

      // Re-authenticate the user with the current username
      UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(userModel.getUsername());
      Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserDetails, null,
          updatedUserDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(newAuth);

      return "Email updated successfully";
    } else {
      return "No authenticated user found";
    }
  }

  // UPDATE PASSWORD
  public String updatePassword(String currentPassword, String newPassword, String confirmNewPassword) {
    if (currentPassword == null || currentPassword.isEmpty() ||
        newPassword == null || newPassword.isEmpty() ||
        confirmNewPassword == null || confirmNewPassword.isEmpty()) {
      return "All fields are required";
    }

    if (!newPassword.equals(confirmNewPassword)) {
      return "New passwords do not match";
    }

    Optional<UserModel> user = getCurrentUser();
    if (user.isPresent()) {
      UserModel userModel = user.get();
      if (passwordEncoder.matches(currentPassword, userModel.getPassword())) {
        userModel.setPassword(passwordEncoder.encode(newPassword));
        repository.save(userModel);

        // Re-authenticate the user with the new password
        UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(userModel.getUsername());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserDetails, null,
            updatedUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "Password updated successfully";
      } else {
        return "Current password is incorrect";
      }
    } else {
      return "No authenticated user found";
    }
  }

  // UPDATE USER PROFILE
  public String updateUserProfile(Map<String, String> params) {
    Optional<UserModel> user = getCurrentUser();
    if (user.isPresent()) {
      // Update user details
      UserModel userModel = user.get();

      userModel.setFirstName(params.get("firstName"));
      userModel.setLastName(params.get("lastName"));
      userModel.setEmail(params.get("email"));
      userModel.setWebsite(params.get("website"));
      userModel.setLocation(params.get("location"));
      userModel.setBiography(params.get("biography"));
      repository.save(userModel);

      // Re-authenticate the user with the updated details
      UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(userModel.getUsername());
      Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserDetails, null,
          updatedUserDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(newAuth);

      return "User profile updated successfully";
    } else {
      return "No authenticated user found";
    }
  }

  // UPDATE PROFILE PICTURE
  public String updateProfilePicture(MultipartFile profilePicture) {
    if (profilePicture.isEmpty()) {
      return "Profile picture is empty";
    }

    try {
      String profilePictureUrl = saveProfilePicture(profilePicture);
      Optional<UserModel> user = getCurrentUser();
      if (user.isPresent()) {
        UserModel userModel = user.get();
        userModel.setProfilePictureUrl(profilePictureUrl);
        repository.save(userModel);

        // Re-authenticate the user with the updated profile picture URL
        UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(userModel.getUsername());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserDetails, null,
            updatedUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "Profile picture updated successfully";
      } else {
        return "User not found";
      }
    } catch (IOException e) {
      return "Failed to update profile picture";
    }
  }

  // SAVE PROFILE PICTURE
  private String saveProfilePicture(MultipartFile profilePicture) throws IOException {
    // Create the directory if it doesn't exist
    File directory = new File("profile-pictures");
    if (!directory.exists()) {
      directory.mkdirs();
    }

    // Check if the file is empty
    if (profilePicture.isEmpty()) {
      throw new IOException("File is empty");
    }
    // Check if the file is too large
    if (profilePicture.getSize() > 5 * 1024 * 1024) { // 5 MB limit
      throw new IOException("File is too large");
    }
    // Check if the file type is allowed
    String contentType = profilePicture.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new IOException("Invalid file type");
    }

    // GENERATE A UNIQUE FILENAME
    String filename = UUID.randomUUID().toString() + "-" + profilePicture.getOriginalFilename();
    File file = new File(directory, filename);

    // Save the file to the directory
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write(profilePicture.getBytes());
    }

    // RETURN IMAGE URL
    return "/profile-pictures/" + filename;
  }

  // CREATE NEW USER
  public Map<String, String> postUser(Map<String, String> params) {
    String username = params.get("username");
    String password = params.get("password");
    String confirmPassword = params.get("confirmPassword");

    Optional<UserModel> userUsername = repository.findByUsername(username);

    List<String> errors = new ArrayList<>();
    if (username == null || username.isEmpty() ||
        password == null || password.isEmpty() ||
        confirmPassword == null || confirmPassword.isEmpty()) {
      errors.add("All fields are required");
    }
    if (userUsername.isPresent()) {
      errors.add("Username already exists");
    }
    if (password == null || !password.equals(confirmPassword)) {
      errors.add("Passwords do not match");
    }

    if (!errors.isEmpty()) {
      Map<String, String> result = new HashMap<>();
      result.put("status", "error");
      result.put("message", String.join(",", errors));
      return result;
    }

    UserModel user = new UserModel();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setRoles(Collections.singletonList("USER"));
    repository.save(user);

    Map<String, String> result = new HashMap<>();
    result.put("status", "success");
    result.put("message", "User registered successfully");
    return result;
  }

  // LOGIN USER
  public String login(Map<String, String> params) {
    // GET FORM DATA PARAMS
    String username = params.get("username");
    String password = params.get("password");

    if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
      return "Username and password are required";
    }

    Optional<UserModel> user = repository.findByUsername(username);
    if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(auth);
      return "Login successful";
    } else {
      return "Invalid username or password";
    }
  }

  // LOGOUT USER
  public String logout() {
    SecurityContextHolder.clearContext();
    return "login";
  }

  // DELETE USER
  public String deleteUser(String username) {
    Optional<UserModel> user = repository.findByUsername(username);
    if (user.isPresent()) {
      repository.delete(user.get());
      return "User deleted successfully";
    } else {
      return "User not found";
    }
  }
}