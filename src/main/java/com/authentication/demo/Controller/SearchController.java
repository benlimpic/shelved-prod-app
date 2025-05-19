package com.authentication.demo.Controller;

import java.util.Collections;
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
        List<UserModel> users = Collections.emptyList();
        List<CollectionModel> collections = Collections.emptyList();

        if (query != null && !query.trim().isEmpty()) {
            String trimmedQuery = query.trim();
            if (trimmedQuery.startsWith("@")) {
                String username = trimmedQuery.substring(1);
                users = userService.searchUsersByUsername(username);
                collections = collectionService.searchCollectionsByTitle(username);
            } else if (trimmedQuery.startsWith("#")) {
                String title = trimmedQuery.substring(1);
                collections = collectionService.searchCollectionsByTitle(title);
                users = userService.searchUsersByUsername(title);
            } else {
                users = userService.searchUsersByUsername(trimmedQuery);
                collections = collectionService.searchCollectionsByTitle(trimmedQuery);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("collections", collections);

        return ResponseEntity.ok(response);
    }
}