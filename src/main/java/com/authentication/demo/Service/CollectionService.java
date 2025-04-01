package com.authentication.demo.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Repository.CollectionRepository;
import com.authentication.demo.Repository.UserRepository;

@Service
public class CollectionService {

  private final CollectionRepository repository;
  private final UserRepository userRepository;

  public CollectionService(CollectionRepository repository, UserRepository userRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
  }

  // CREATE COLLECTION
  public Map<String, String> createCollection(Map<String, String> params, MultipartFile collectionImage) {
    // GET CURRENT USER
    SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext.getAuthentication() == null || !securityContext.getAuthentication().isAuthenticated()) {
      return Map.of("error", "No User Found");
    }

    // GET CURRENT USER ID
    Long userId = userRepository.findByUsername(securityContext.getAuthentication().getName())
        .map(user -> user.getId())
        .orElse(null);

    // GET COLLECTION DETAILS
    String imageUrl = saveCollectionImage(collectionImage);
    String caption = params.get("caption");
    String description = params.get("description");

    // GET CURRENT TIMESTAMP
    Timestamp createdAt = new Timestamp(System.currentTimeMillis());
    Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

    // COLLECTION CONSTRUCTOR
    CollectionModel newCollection = new CollectionModel(
        null, userId, caption, description, imageUrl, createdAt, updatedAt);

    // SAVE COLLECTION
    repository.save(newCollection);
    return Map.of("message", "Collection created successfully");
  }


  // SAVE PROFILE PICTURE
  public String saveCollectionImage(MultipartFile collectionImage) {
    // Create the directory if it doesn't exist
    File directory = new File("collection-images");
    if (!directory.exists()) {
      directory.mkdirs();
    }

    // Ensure the directory for collection images exists
    File collectionImageDir = new File("collection-images");
    if (!collectionImageDir.exists()) {
      collectionImageDir.mkdirs();
    }

    // GENERATE A UNIQUE FILENAME
    String filename = UUID.randomUUID().toString() + "-" + collectionImage.getOriginalFilename();
    File file = new File(directory, filename);

    // Save the file to the directory
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write(collectionImage.getBytes());
    } catch (Exception e) {
      throw new RuntimeException("Failed to save collection image", e);
    }
    

    // RETURN IMAGE URL
    return "/collection-images/" + filename;
  }

}
