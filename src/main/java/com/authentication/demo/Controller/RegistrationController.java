package com.authentication.demo.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.authentication.demo.Service.UserService;
import com.authentication.demo.logger.AuthenticationLogger;

@Controller
public class RegistrationController {

  private final UserService userService;

  public RegistrationController(UserService userService) {
    this.userService = userService;
  }

  //REGISTER NEW USER
  @PostMapping(value = "/req/signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public String postUser(@RequestParam Map<String, String> params, Model model) {
    // CREATE ERROR LIST
    List<String> errors = new ArrayList<>();

    // CHECK IF FORM DATA IS NOT EMPTY
    if (params != null && !params.isEmpty()) {

      try {
        userService.postUser(params);
        // AUTHENTICATE USER
        UserDetails userDetails = userService.loadUserByUsername(params.get("username"));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // SET AUTHENTICATION
        SecurityContextHolder.getContext().setAuthentication(auth);

        // LOG AUTHENTICATION DETAILS
        System.out.println("User created successfully");
        AuthenticationLogger.logAuthenticationDetails();

        // REDIRECT TO HOME PAGE
        return "index";
      }

      // SIGNUP PAGE ERROR HANDLING
      catch (IllegalArgumentException e) {
        String[] errorArray = e.getMessage().split(", ");
        for (String error : errorArray) {
          errors.add(error);
        }
      } catch (Exception e) {
        errors.add("An unexpected error occurred. Please try again.");
      }
    }
    // RETURN TO SIGNUP PAGE WITH ERRORS
    model.addAttribute("errors", errors);
    System.out.println("User creation failed");
    AuthenticationLogger.logAuthenticationDetails();
    return "signup";
  }
}