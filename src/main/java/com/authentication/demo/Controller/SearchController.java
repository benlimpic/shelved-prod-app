package com.authentication.demo.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Service.CollectionService;
import com.authentication.demo.Service.UserService;

@RestController
@RequestMapping("/search-live")
public class SearchController {

  private final UserService userService;
  private final CollectionService collectionService;

  public SearchController(UserService userService, CollectionService collectionService) {
    this.userService = userService;
    this.collectionService = collectionService;
  }

  @GetMapping
  public ResponseEntity<Map<String, Object>> liveSearch(@RequestParam(required = false) String query) {
    List<UserModel> users;
    List<CollectionModel> collections;

    if (query == null || query.trim().isEmpty()) {
      // If no query is provided, return empty lists
      users = new java.util.ArrayList<>();
      collections = new java.util.ArrayList<>();
    } else if (query.startsWith("@")) {
      // Search for users by username
      String username = query.substring(1);
      users = userService.searchUsersByUsername(username);
      // Search for collections by title
      collections = collectionService.searchCollectionsByTitle(username);
    } else if (query.startsWith("#")) {
      // Search for collections by title
      String title = query.substring(1);
      collections = collectionService.searchCollectionsByTitle(title);
      // Search for users by username
      users = userService.searchUsersByUsername(title);
    } else {
      // Search for users by username
      users = userService.searchUsersByUsername(query);
      // Search for collections by title
      collections = collectionService.searchCollectionsByTitle(query);
    }

    // Prepare the response
    Map<String, Object> response = new HashMap<>();
    response.put("users", users);
    response.put("collections", collections);

    return ResponseEntity.ok(response);
  }
}