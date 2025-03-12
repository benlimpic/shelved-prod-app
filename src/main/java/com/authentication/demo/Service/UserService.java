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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
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

import com.authentication.demo.Model.CustomUserDetails;
import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;

  public UserService(UserRepository repository, @Lazy PasswordEncoder passwordEncoder,
      UserDetailsService userDetailsService) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
  }

  @Lazy
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserModel> user = repository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    List<GrantedAuthority> authorities = user.get().getRoles().stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toList());

    return new CustomUserDetails(
        user.get().getUsername(),
        user.get().getPassword(),
        user.get().getEmail(),
        user.get().getFirstName(),
        user.get().getLastName(),
        user.get().getBiography(),
        user.get().getProfilePictureUrl(),
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
    Optional<UserModel> existingUser = repository.findByUsername(newUsername);
    if (existingUser.isPresent()) {
      return "Username already exists";
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String currentUsername = userDetails.getUsername();

      Optional<UserModel> user = repository.findByUsername(currentUsername);
      if (user.isPresent()) {
        UserModel userModel = user.get();
        userModel.setUsername(newUsername);
        repository.save(userModel);

        // Re-authenticate the user with the new username
        UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(newUsername);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserDetails,
            authentication.getCredentials(), updatedUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "Username updated successfully";
      } else {
        return "User not found";
      }
    } else {
      return "No authenticated user found";
    }
  }

  // UPDATE EMAIL
  public String updateEmail(String newEmail, String confirmNewEmail) {
    if (!newEmail.equals(confirmNewEmail)) {
      return "New emails do not match";
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
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserDetails, newPassword,
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

  //UPDATE USER DETAILS
  public String updateUserDetails(Map<String, String> params) {
    Optional<UserModel> user = getCurrentUser();
    if (user.isPresent()) {
      UserModel userModel = user.get();
      userModel.setFirstName(params.get("firstName"));
      userModel.setLastName(params.get("lastName"));
      userModel.setBiography(params.get("biography"));
      repository.save(userModel);

      // Re-authenticate the user with the updated details
      UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(userModel.getUsername());
      Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserDetails, null,
          updatedUserDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(newAuth);

      return "User details updated successfully";
    } else {
      return "No authenticated user found";
    }
  }

  // CREATE NEW USER
  public Map<String, String> postUser(Map<String, String> params) {
    String username = params.get("username");
    String email = params.get("email");
    String password = params.get("password");
    String confirmPassword = params.get("confirmPassword");

    Optional<UserModel> userUsername = repository.findByUsername(username);
    Optional<UserModel> userEmail = repository.findByEmail(email);

    List<String> errors = new ArrayList<>();
    if (username == null || username.isEmpty() ||
        email == null || email.isEmpty() ||
        password == null || password.isEmpty() ||
        confirmPassword == null || confirmPassword.isEmpty()) {
      errors.add("All fields are required");
    }
    if (userUsername.isPresent()) {
      errors.add("Username already exists");
    }
    if (userEmail.isPresent()) {
      errors.add("Email already exists");
    }
    if (!password.equals(confirmPassword)) {
      errors.add("Passwords do not match");
    }

    if (!errors.isEmpty()) {
      Map<String, String> result = new HashMap<>();
      result.put("status", "error");
      result.put("message", String.join(", ", errors));
      return result;
    }

    UserModel user = new UserModel();
    user.setEmail(email);
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setRoles(Collections.singletonList("USER"));
    user.setProfilePictureUrl(getDefaultProfilePictureUrl());
    repository.save(user);

    Map<String, String> result = new HashMap<>();
    result.put("status", "success");
    result.put("message", "User registered successfully");
    return result;
  }

  // LOGIN USER
  public void login(Map<String, String> params) {
    // GET FORM DATA PARAMS
    String username = params.get("username");
    String password = params.get("password");

  }

  // LOGOUT USER
  public String logout() {
    SecurityContextHolder.clearContext();
    return "login";
  }

  //UPDATE PROFILE PICTURE
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

  //SAVE PROFILE PICTURE
  private String saveProfilePicture(MultipartFile profilePicture) throws IOException {
    // Create the directory if it doesn't exist
    File directory = new File("profile-pictures");
    if (!directory.exists()) {
      directory.mkdirs();
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

  //GET DEFAULT PROFILE PICTURE
  public String getDefaultProfilePictureUrl() {
    return "/profile-pictures/default-profile-photo.png";
  }

  // DELETE USER
  public String deleteUser(String username) {
    Optional<UserModel> user = repository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    repository.delete(user.get());
    return "signup";
  }
}