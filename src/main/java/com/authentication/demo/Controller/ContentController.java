package com.authentication.demo.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.UserRepository;

@Controller
public class ContentController {

  @Autowired
  UserRepository repository;

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
      return handleAuthentication(model, "index");
  }
  
  @GetMapping("/profile")
  public String profile(Model model) {
      return handleAuthentication(model, "profile");
  }
  
  @GetMapping("/update-profile")
  public String updateProfile(Model model) {
      return handleAuthentication(model, "updateProfile");
  }
  
  @GetMapping("/update-user-details")
  public String updateUserDetails(Model model) {
      return handleAuthentication(model, "updateUserDetails");
  }

  private String handleAuthentication(Model model, String viewName) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getName() != null) {
        Optional<UserModel> user = repository.findByUsername(authentication.getName());
        if (user.isPresent()) {
            UserModel userModel = user.get();
            model.addAttribute("user", userModel);
            return viewName;
        }
    }
    return "redirect:/login";
  }

}
