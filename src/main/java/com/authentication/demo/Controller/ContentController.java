package com.authentication.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
  public String index() {
    return "index";
  }

  @GetMapping("/profile")
  public String profile() {
    return "profile";
  }
}