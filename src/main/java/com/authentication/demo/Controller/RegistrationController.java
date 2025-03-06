package com.authentication.demo.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.authentication.demo.Service.UserService;

@Controller
public class RegistrationController {

  private final UserService userService;

  public RegistrationController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(value = "/req/signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public String postUser(@RequestParam Map<String, String> params, Model model) {
    List<String> errors = new ArrayList<>();
    if (params == null || params.isEmpty()) {
      errors.add("Parameters cannot be null or empty.");
      model.addAttribute("errors", errors);
      return "signup"; // Return the view name for the signup page
    }
    try {
      userService.postUser(params);
      // Authenticate the user after successful registration
      UserDetails userDetails = userService.loadUserByUsername(params.get("username"));
      Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(auth);
      System.out.println("User created and authenticated successfully");
      return "index"; // Redirect to the index page
    } catch (IllegalArgumentException e) {
      String[] errorArray = e.getMessage().split(", ");
      for (String error : errorArray) {
        errors.add(error);
      }
    } catch (Exception e) {
      errors.add("An unexpected error occurred. Please try again.");
    }
    model.addAttribute("errors", errors);
    return "signup"; // Return the view name for the signup page
  }
}