package com.authentication.demo.Controller;

import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.authentication.demo.Service.UserService;
import com.authentication.demo.logger.AuthenticationLogger;

@Controller
@RequestMapping("/req")
public class RegistrationController {

  private final UserService userService;
  private final AuthenticationManager authenticationManager;

  public RegistrationController(@Lazy UserService userService, AuthenticationManager authenticationManager, AuthenticationLogger logger) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/signup")
  public String postUser(@RequestParam Map<String, String> userDetails) {

    userService.postUser(userDetails);

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails.get("username"),
        userDetails.get("password"));
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    AuthenticationLogger.logAuthenticationDetails();

    return "index";
  }
}