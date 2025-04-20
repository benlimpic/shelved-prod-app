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
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
  public Optional<UserModel> getCurrentUser() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext.getAuthentication() == null || !securityContext.getAuthentication().isAuthenticated()) {
      return Optional.empty();
    }

    String username = securityContext.getAuthentication().getName();
    return repository.findByUsername(username);
  }

  // GET CURRENT USER ID
  public Long getCurrentUserId() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext.getAuthentication() == null || !securityContext.getAuthentication().isAuthenticated()) {
      throw new RuntimeException("No authenticated user found");
    }

    String username = securityContext.getAuthentication().getName();
    return repository.findByUsername(username)
        .map(user -> user.getId())
        .orElseThrow(() -> new RuntimeException("User not found for username: " + username));
  }


  //Get User By ID
  public UserModel getUserById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
  }


  // UPDATE USERNAME
  public String updateUsername(String newUsername) {
    if (newUsername == null || newUsername.isEmpty()) {
      return "Username is required";
    }

    // Check if the new username already exists
    Optional<UserModel> existingUser = repository.findByUsername(newUsername);
    if (existingUser.isPresent()) {
      return "Username already exists";
    }

    // Check if the new username is the same as the current username
    Optional<UserModel> currentUser = getCurrentUser();
    if (currentUser.isPresent() && currentUser.get().getUsername().equals(newUsername)) {
      return "New username is the same as the current username";
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
      return "Email is required";
    }

    if (!newEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      return "Invalid email format";
    }

    if (!newEmail.equals(confirmNewEmail)) {
      return "Emails do not match";
    }

    Optional<UserModel> user = getCurrentUser();
    if (user.isPresent()) {
      UserModel userModel = user.get();
      userModel.setEmail(newEmail);
      repository.save(userModel);

      // Re-authenticate the user with the new email
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

  // UPDATE NAME
  public String updateName(Map<String, String> params) {
    Optional<UserModel> user = getCurrentUser();
    if (user.isPresent()) {
      // Update user details
      UserModel userModel = user.get();

      userModel.setFirstName(params.get("firstName"));
      userModel.setLastName(params.get("lastName"));
      repository.save(userModel);

      // Re-authenticate the user with the updated details
      UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(userModel.getUsername());
      Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserDetails, null,
          updatedUserDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(newAuth);

      return "Name updated successfully";
    } else {
      return "No authenticated user found";
    }
  }

  // UPDATE USER PROFILE
  public String updateProfile(Map<String, String> userDetails, MultipartFile profilePicture, RedirectAttributes redirectAttributes) {
    try {
      // Fetch the current user
      Optional<UserModel> user = getCurrentUser();
      if (user.isPresent()) {
        UserModel userModel = user.get();
  
        // Update profile picture if provided
        if (profilePicture != null && !profilePicture.isEmpty()) {
          String profilePictureUrl = saveProfilePicture(profilePicture);
          userModel.setProfilePictureUrl(profilePictureUrl);
        }
  
        // Update other user details
        userModel.setLocation(userDetails.get("location"));
        userModel.setWebsite(userDetails.get("website"));
        userModel.setBiography(userDetails.get("biography"));
  
        // Save the updated user back to the database
        repository.save(userModel);
  
        return "User profile updated successfully";
      } else {
        return "User not found";
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to update profile", e);
    }
  }

  // SAVE PROFILE PICTURE
  public String saveProfilePicture(MultipartFile profilePicture) throws IOException {
    // Create the directory if it doesn't exist
    File directory = new File("profile-pictures");
    if (!directory.exists()) {
      directory.mkdirs();
    }
  
    // Generate a unique filename
    String filename = UUID.randomUUID().toString() + "-" + profilePicture.getOriginalFilename();
    File file = new File(directory, filename);
  
    // Save the file
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write(profilePicture.getBytes());
    }
  
    // Return the file URL
    return "/profile-pictures/" + filename;
  }

  // CREATE NEW USER
  public Map<String, String> postUser(Map<String, String> params) {
    String username = params.get("username");
    String email = params.get("email");
    String password = params.get("password");
    String confirmPassword = params.get("confirmPassword");
    String firstNameString = params.get("firstName");
    String lastNameString = params.get("lastName");

    Optional<UserModel> userUsername = repository.findByUsername(username);

    List<String> errors = new ArrayList<>();
    if (username == null || username.isEmpty() ||
        password == null || password.isEmpty() ||
        email == null || email.isEmpty() ||
        confirmPassword == null || confirmPassword.isEmpty()) {
      errors.add("All fields are required");
    }
    if (userUsername.isPresent()) {
      errors.add("Username already exists");
    }

    if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      errors.add("Invalid email format");
    }

    if (email != null && repository.findByEmail(email).isPresent()) {
      errors.add("Email already exists");
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
    user.setEmail(email);
    user.setFirstName(firstNameString);
    user.setLastName(lastNameString);
    user.setRoles(Collections.singletonList("USER"));
    repository.save(user);

    Map<String, String> result = new HashMap<>();
    result.put("status", "success");
    result.put("message", "User registered successfully");
    return result;
  }

  // LOGIN USER
  public String login(Map<String, String> params) {
    // Get form data parameters
    String username = params.get("username");
    String password = params.get("password");

    // Validate input
    if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
      return "Username and password are required";
    }

    // Find user by username
    Optional<UserModel> user = repository.findByUsername(username);
    if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {

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

  // GET USERS BY ID
  public Map<Long, UserModel> getUsersByIds(List<Long> userIds) {
    return repository.findAllById(userIds).stream()
        .collect(Collectors.toMap(UserModel::getId, Function.identity()));
}

  // GET ALL USERS
  public List<UserModel> getAllUsers() {
    return repository.findAll();
  }

  // GET USER BY USERNAME
  public Optional<UserModel> getUserByUsername(String username) {
    return repository.findByUsername(username);
  }

  // GET USER BY EMAIL
  public Optional<UserModel> getUserByEmail(String email) {
    return repository.findByEmail(email);
  }

  public Map<Long, UserModel> getUsersMappedById() {
    List<UserModel> users = repository.findAll();
    return users.stream().collect(Collectors.toMap(UserModel::getId, Function.identity()));
}

}