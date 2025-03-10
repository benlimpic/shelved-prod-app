package com.authentication.demo.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public UserService(UserRepository repository, @Lazy PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Lazy
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

  // LOAD USER AND CREATE USERDETAILS OBJECT
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserModel> user = repository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    // CONVERT ROLES TO GRANTEDAUTHORITY OBJECT
    List<GrantedAuthority> authorities = user.get().getRoles().stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toList());

    // CREATE USERDETAILS OBJECT
    return new User(user.get().getUsername(), user.get().getPassword(), authorities);
  }


  // UPDATE USERNAME
  public String updateUsername(String username, String newUsername) {
    Optional<UserModel> user = repository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    Optional<UserModel> userNew = repository.findByUsername(newUsername);
    if (userNew.isPresent()) {
      throw new IllegalArgumentException("Username already exists");
    }

    user.get().setUsername(newUsername);
    repository.save(user.get());
    return "profile";
  }


  // UPDATE EMAIL
  public String updateEmail(String email, String newEmail) {
    Optional<UserModel> user = repository.findByEmail(email);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found with email: " + email);
    }

    Optional<UserModel> userNew = repository.findByEmail(newEmail);
    if (userNew.isPresent()) {
      throw new IllegalArgumentException("Email already exists");
    }

    user.get().setEmail(newEmail);
    repository.save(user.get());
    return "profile";
  }


  // UPDATE PASSWORD
  public String updatePassword(String username, String password, String newPassword, String newConfirmPassword) {
    Optional<UserModel> user = repository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    if (!passwordEncoder.matches(password, user.get().getPassword())) {
      throw new IllegalArgumentException("Incorrect password");
    }

    if (!newPassword.equals(newConfirmPassword)) {
      throw new IllegalArgumentException("Passwords do not match");
    }

    user.get().setPassword(passwordEncoder.encode(newPassword));
    repository.save(user.get());
    return "profile";
  }

  // UPDATE PROFILE PICTURE
  public String updateProfilePicture(String username, String profilePicture) {
    Optional<UserModel> user = repository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    user.get().setProfilePicture(profilePicture);
    repository.save(user.get());
    return "profile";
  }


  // UPDATE USER DETAILS
  public String updateUserDetails(String username, String firstName, String lastName, String birthday, String aboutMe) {
    Optional<UserModel> user = repository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    user.get().setFirstName(firstName);
    user.get().setLastName(lastName);
    user.get().setBirthday(birthday);
    user.get().setAboutMe(aboutMe);
    repository.save(user.get());
    return "profile";
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


    // AUTHENTICATE USER
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return "index";
  }


  //LOGOUT USER
  public String logout() {
    SecurityContextHolder.clearContext();
    return "login";
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