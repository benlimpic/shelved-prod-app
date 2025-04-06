package com.authentication.demo.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
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

public Map<String, String> createCollection(Map<String, String> params, MultipartFile collectionImage) {
    try {

        // Save collection logic
        String imageUrl = saveCollectionImage(collectionImage);
        Long userId = userService.getCurrentUserId();
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

        CollectionModel newCollection = new CollectionModel(
            null, userId, params.get("caption"), params.get("description"), imageUrl, createdAt, updatedAt);

        repository.save(newCollection);
        return Map.of("message", "Collection created successfully");
    } catch (Exception e) {
        throw new CollectionCreationException("Failed to create collection", e);
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

}
