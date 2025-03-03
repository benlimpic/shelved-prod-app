package com.authentication.demo.Controller;

import java.util.Map;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authentication.demo.Model.MyAppUser;
import com.authentication.demo.Repository.MyAppUserRepository;

@RestController
public class RegistrationController {

  @Autowired
  private MyAppUserRepository myAppUserRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping(value = "/req/signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<String> handleFormSubmit(@RequestParam Map<String, String> params) {
    MyAppUser user = new MyAppUser();
    user.setEmail(params.get("email"));
    user.setUsername(params.get("username"));
    user.setPassword(passwordEncoder.encode(params.get("password")));
    myAppUserRepository.save(user);
    return ResponseEntity.status(HttpStatus.FOUND)
                         .location(URI.create("/req/login"))
                         .body("User created");
  }
}