package com.authentication.demo.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

  @GetMapping("/req/login")
  public String login() {

    if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
      return "login";
    }
    return "redirect:/index";
  }

  @GetMapping("/req/signup")
  public String signup() {
    return "signup";
  }

  @GetMapping("/index")
  public String home() {
    return "index";
  }

}

// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
// if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
//   return "redirect:/index";
// }
// return "login";