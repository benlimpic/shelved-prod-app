package com.authentication.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.UserRepository;
import com.authentication.demo.Service.CollectionService;

@Controller
public class ContentController {

  @Autowired
  UserRepository repository;
  @Autowired
  CollectionService collectionService;

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
        // FETCH SORTED COLLECTIONS BY USER ID
        List<CollectionModel> collections = collectionService.getCollectionsForCurrentUser();
        
        // BREAK COLLECTIONS INTO GROUPS OF 3
        List<List<CollectionModel>> partitionedCollections = ListUtils.partition(collections, 3);

        // ADD TO MODEL
        model.addAttribute("partitionedCollections", partitionedCollections);
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

  @GetMapping("/create-collection")
  public String createCollection(Model model) {
      return handleAuthentication(model, "createCollection");
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
