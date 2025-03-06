package com.authentication.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authentication.demo.Model.MyAppUser;
import com.authentication.demo.Repository.MyAppUserRepository;

@Service
public class MyAppUserService implements UserDetailsService {

  private final MyAppUserRepository repository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public MyAppUserService(MyAppUserRepository repository, @Lazy PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<MyAppUser> user = repository.findByUsername(username);

    if (user.isPresent()) {
      var userObj = user.get();
      return User.builder()
          .username(userObj.getUsername())
          .password(userObj.getPassword())
          .build();
    }

    throw new UsernameNotFoundException(username);
  }

  // POST METHOD TO CREATE NEW USER
  public void postUser(Map<String, String> params) {
    // GET FORM DATA PARAMS
    String username = params.get("username");
    String email = params.get("email");
    String password = params.get("password");
    String confirmPassword = params.get("confirmPassword");

    // CHECK IF USERNAME OR EMAIL ALREADY EXISTS
    Optional<MyAppUser> userUsername = repository.findByUsername(username);
    Optional<MyAppUser> userEmail = repository.findByEmail(email);

    List<String> errors = new ArrayList<>();
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
    MyAppUser user = new MyAppUser();
    user.setEmail(email);
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    repository.save(user);

    // AUTHORIZE NEW USER
    UserDetails userDetails = loadUserByUsername(username);
    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
    System.out.println("User created and authorized");
  }
}