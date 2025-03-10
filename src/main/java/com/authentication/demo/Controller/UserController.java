package com.authentication.demo.Controller;

import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.authentication.demo.Service.UserService;

@Controller
public class UserController {

  private final UserService userService;
  private final AuthenticationManager authenticationManager;

  public UserController(@Lazy UserService userService, AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
  }


  @PostMapping("/signup")
  public String createUser(@RequestParam Map<String, String> userDetails, RedirectAttributes redirectAttributes) {
    Map<String, String> result = userService.postUser(userDetails);

    if ("error".equals(result.get("status"))) {
      redirectAttributes.addFlashAttribute("error", result.get("message"));
      return "redirect:/signup";
    }

    redirectAttributes.addFlashAttribute("message", result.get("message"));
    return "redirect:/login";
  }

  @PutMapping("/update_user_details")
  public String updateUser(@RequestBody Map<String, String> userDetails) {
    return "profile";
  }

  @PutMapping("/update_password")
  public String updatePassword(@RequestBody Map<String, String> userDetails) {
    return "profile";
  }

  @PutMapping("/update_username")
  public String updateUsername(@RequestBody Map<String, String> userDetails) {
    return "profile";
  }

  @PutMapping("/update_email")
  public String updateEmail(@RequestBody Map<String, String> userDetails) {
    return "profile";
  }

  @PostMapping("/login")
  public String login() {
    return "index";
  }

  @PostMapping("/logout")
  public String logout() {
    return "login";
  }

  @DeleteMapping("/delete")
  public String deleteUser() {
    return "login";
  }
}