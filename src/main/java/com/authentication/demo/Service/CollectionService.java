package com.authentication.demo.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.authentication.demo.Exceptions.CollectionCreationException;
import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Repository.CollectionRepository;

@Service
public class CollectionService {

  private static final Logger logger = LoggerFactory.getLogger(CollectionService.class);

  private final CollectionRepository repository;
  private final UserService userService;

  public CollectionService(CollectionRepository repository, UserService userService) {
    this.userService = userService;
    this.repository = repository;
  }

  // GET COLLECTION BY COLLECTION ID
  public CollectionModel getCollectionById(Long id) {
    CollectionModel collection = repository.findById(id).orElse(null);
    if (collection == null) {
      throw new CollectionCreationException("Collection not found");
    }
    return collection;
  }

  // CREATE COLLECTION
  public Map<String, String> createCollection(Map<String, String> params, MultipartFile collectionImage) {
    try {
      // Validate input parameters
      if (collectionImage == null || collectionImage.isEmpty()) {
        throw new CollectionCreationException("Collection image is required");
      }

      // Save collection logic
      String imageUrl = saveCollectionImage(collectionImage);
      Long userId = userService.getCurrentUserId();
      Timestamp createdAt = new Timestamp(System.currentTimeMillis());
      Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
      
      CollectionModel newCollection = new CollectionModel(
          null, userId, params.get("caption"), params.get("description"), imageUrl, createdAt, updatedAt
      );

      repository.save(newCollection);
      return Map.of("message", "Collection created successfully");
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new CollectionCreationException("Invalid input parameters", e);
    } catch (RuntimeException e) {
      throw new CollectionCreationException("Unexpected error occurred while creating collection", e);
    }
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

  // GET COLLECTIONS FOR CURRENT USER
  public List<CollectionModel> getCollectionsForCurrentUser() {
    // Fetch the current user's ID using UserService
    Long userId = userService.getCurrentUserId();

    // RETURN COLLECTION BY USER ID AND SORTED BY CREATED AT DESC
    return repository.findAllByUserIdOrderByCreatedAtDesc(userId);
  }

}
