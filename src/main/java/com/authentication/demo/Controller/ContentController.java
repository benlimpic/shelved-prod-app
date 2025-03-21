package com.authentication.demo.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.authentication.demo.Model.CustomUserDetails;

@Controller
public class ContentController {

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/signup")
  public String signup() {
    return "signup";
  }

  @GetMapping("/index")
  public String index(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      model.addAttribute("user", userDetails);
    }
    return "index";
  }

  @GetMapping("/profile")
  public String profile(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      model.addAttribute("user", userDetails);
    }
    return "profile";
  }

  @GetMapping("/update-profile")
  public String updateProfile(Model model) {
      // Ensure the user object is added to the model
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
          CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
          model.addAttribute("user", userDetails);
      }
      return "updateProfile";
  }

}