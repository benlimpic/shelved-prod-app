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
  public ResponseEntity<Map<String, Object>> liveSearch(@RequestParam("query") String query) {
    // Search for users by username
    List<UserModel> users = userService.searchUsersByUsername(query);

    // Search for collections by title
    List<CollectionModel> collections = collectionService.searchCollectionsByTitle(query);

    // Prepare the response
    Map<String, Object> response = new HashMap<>();
    response.put("users", users);
    response.put("collections", collections);

    return ResponseEntity.ok(response);
  }
}