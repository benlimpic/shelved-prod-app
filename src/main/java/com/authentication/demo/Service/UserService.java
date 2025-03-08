package com.authentication.demo.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
  private final AuthenticationManager authenticationManager;


  public UserService(UserRepository repository, @Lazy PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }


  // LOAD USER BY USERNAME
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Fetch user from database
    Optional<UserModel> user = repository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found");
    }

    // RETURN USER DETAILS
    return User.withUsername(user.get().getUsername())
        .password(user.get().getPassword())
        .authorities(user.get().getRoles().isEmpty() ? List.of(new SimpleGrantedAuthority("USER"))
            : user.get().getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
        .build();
  }

  // POST METHOD TO CREATE NEW USER
  public void postUser(Map<String, String> params) {
    // GET FORM DATA PARAMS
    String username = params.get("username");
    String email = params.get("email");
    String password = params.get("password");
    String confirmPassword = params.get("confirmPassword");

    // CHECK IF USERNAME OR EMAIL ALREADY EXISTS
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
      throw new IllegalArgumentException(String.join(", ", errors));
    }

    // CREATE NEW USER
    UserModel user = new UserModel();
    user.setEmail(email);
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setRoles(Collections.singletonList("USER"));
    repository.save(user);
    

    // Authenticate new user
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}