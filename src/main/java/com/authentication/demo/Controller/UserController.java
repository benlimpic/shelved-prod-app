package com.authentication.demo.Controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.authentication.demo.Service.UserService;

@Controller
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);
  private final UserService userService;
  private final AuthenticationManager authenticationManager;

  public UserController(@Lazy UserService userService, AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/signup")
  public String createUser(@RequestParam Map<String, String> userDetails,
      RedirectAttributes redirectAttributes) {

    Map<String, String> result = userService.postUser(userDetails);

    if ("error".equals(result.get("status"))) {
      redirectAttributes.addFlashAttribute("error", result.get("message"));
      return "redirect:/signup";
    }

    redirectAttributes.addFlashAttribute("message", result.get("message"));
    return "redirect:/login";
  }

  @PostMapping("/login")
  public String login(@RequestParam String username, @RequestParam String password,
      RedirectAttributes redirectAttributes) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      return "redirect:/profile";
    } catch (AuthenticationException e) {
      redirectAttributes.addFlashAttribute("error", "Invalid username or password");
      return "redirect:/login";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "An unexpected error occurred");
      return "redirect:/login";
    }
  }

  @PostMapping("/logout")
  public String logout() {
    return "login";
  }

  @PostMapping("/update_username")
  public String updateUsername(@RequestParam String username, RedirectAttributes redirectAttributes) {
    String result = userService.updateUsername(username);
    if ("Username updated successfully".equals(result)) {
      redirectAttributes.addFlashAttribute("message", result);
    } else {
      redirectAttributes.addFlashAttribute("error", result);
    }
    return "redirect:/profile";
  }

  @PostMapping("/update_email")
  public String updateEmail(@RequestParam String newEmail, @RequestParam String confirmNewEmail,
      RedirectAttributes redirectAttributes) {
    String result = userService.updateEmail(newEmail, confirmNewEmail);
    if ("Email updated successfully".equals(result)) {
      redirectAttributes.addFlashAttribute("message", result);
    } else {
      redirectAttributes.addFlashAttribute("error", result);
    }
    return "redirect:/profile";
  }

  @PostMapping("/update_password")
  public String updatePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
      @RequestParam String confirmNewPassword, RedirectAttributes redirectAttributes) {
    String result = userService.updatePassword(currentPassword, newPassword, confirmNewPassword);
    if ("Password updated successfully".equals(result)) {
      redirectAttributes.addFlashAttribute("message", result);
    } else {
      redirectAttributes.addFlashAttribute("error", result);
    }
    return "redirect:/profile";
  }

  @PostMapping("/update_profile_picture")
  public String updateProfilePicture(@RequestParam("profilePicture") MultipartFile profilePicture,
      RedirectAttributes redirectAttributes) {
    String result = userService.updateProfilePicture(profilePicture);
    if ("Profile picture updated successfully".equals(result)) {
      redirectAttributes.addFlashAttribute("message", result);
      return "redirect:/profile";
    } else {
      redirectAttributes.addFlashAttribute("error", result);
      return "redirect:/profile";
    }
  }

  @PostMapping("/update_user_details")
  public String updateUserDetails(@RequestParam Map<String, String> userDetails,
      RedirectAttributes redirectAttributes) {
    String result = userService.updateUserDetails(userDetails);
    if ("User details updated successfully".equals(result)) {
      redirectAttributes.addFlashAttribute("message", result);
      return "redirect:/profile";
    } else {
      redirectAttributes.addFlashAttribute("error", result);
      return "redirect:/profile";
    }
  }

  @DeleteMapping("/delete")
  public String deleteUser() {
    return "login";
  }

}