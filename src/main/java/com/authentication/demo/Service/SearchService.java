package com.authentication.demo.Service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Model.ItemModel;
import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.CollectionRepository;
import com.authentication.demo.Repository.ItemRepository;
import com.authentication.demo.Repository.UserRepository;

@Service
public class SearchService {

  private final UserRepository userRepository;
  private final CollectionRepository collectionRepository;
  private final ItemRepository itemRepository;

  public SearchService(UserRepository userRepository, CollectionRepository collectionRepository,
      ItemRepository itemRepository) {
    this.userRepository = userRepository;
    this.collectionRepository = collectionRepository;
    this.itemRepository = itemRepository;
  }

  // Example method to search users
  public List<UserModel> searchUsers(String query) {
    if (query == null || query.isEmpty()) {
      return Collections.emptyList();
    }

    List<UserModel> allMatches = userRepository.findByUsernameContainingIgnoreCase(query);
    String lowerQuery = query.toLowerCase();

    List<UserModel> startsWith = allMatches.stream()
        .filter(u -> u.getUsername().toLowerCase().startsWith(lowerQuery))
        .sorted((a, b) -> a.getUsername().compareToIgnoreCase(b.getUsername()))
        .toList();

    List<UserModel> contains = allMatches.stream()
        .filter(u -> !u.getUsername().toLowerCase().startsWith(lowerQuery))
        .sorted((a, b) -> a.getUsername().compareToIgnoreCase(b.getUsername()))
        .toList();

    List<UserModel> result = new java.util.ArrayList<>(startsWith);
    result.addAll(contains);
    return result;

  }

  // Example method to search collections
  public List<CollectionModel> searchCollections(String query) {
    if (query == null || query.isEmpty()) {
      return Collections.emptyList();
    }

    List<CollectionModel> allMatches = collectionRepository.findByTitleContainingIgnoreCase(query);
    String lowerQuery = query.toLowerCase();

    List<CollectionModel> startsWith = allMatches.stream()
        .filter(c -> c.getTitle().toLowerCase().startsWith(lowerQuery))
        .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
        .toList();

    List<CollectionModel> contains = allMatches.stream()
        .filter(c -> !c.getTitle().toLowerCase().startsWith(lowerQuery))
        .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
        .toList();

    List<CollectionModel> result = new java.util.ArrayList<>(startsWith);
    result.addAll(contains);
    return result;
  }

  // Example method to search items
  public List<ItemModel> searchItems(String query) {
    if (query == null || query.isEmpty()) {
      return Collections.emptyList();
    }

        List<ItemModel> allMatches = itemRepository.findByTitleContainingIgnoreCase(query);
    String lowerQuery = query.toLowerCase();

    List<ItemModel> startsWith = allMatches.stream()
        .filter(c -> c.getTitle().toLowerCase().startsWith(lowerQuery))
        .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
        .toList();

    List<ItemModel> contains = allMatches.stream()
        .filter(c -> !c.getTitle().toLowerCase().startsWith(lowerQuery))
        .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
        .toList();

    List<ItemModel> result = new java.util.ArrayList<>(startsWith);
    result.addAll(contains);
    return result;
  }

}
