package com.authentication.demo.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.authentication.demo.Exceptions.CollectionCreationException;
import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Model.ItemModel;
import com.authentication.demo.Repository.CollectionRepository;

@Service
public class CollectionService {

  private final CollectionRepository repository;
  private final UserService userService;
  private final ItemService itemService;

  public CollectionService(CollectionRepository repository, UserService userService, ItemService itemService) {
    this.userService = userService;
    this.repository = repository;
    this.itemService = itemService;
  }

  // GET COLLECTION BY COLLECTION ID
  public CollectionModel getCollectionById(Long id) throws CollectionCreationException {
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

      if (params.get("title") == null || params.get("title").isEmpty()) {
        throw new CollectionCreationException("Title is required");
      }

      // Save collection logic
      String imageUrl = saveCollectionImage(collectionImage);
      Long userId = userService.getCurrentUserId();
      Timestamp createdAt = new Timestamp(System.currentTimeMillis());
      Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

      CollectionModel newCollection = new CollectionModel();
      newCollection.setUser(userService.getUserById(userId));
      newCollection.setTitle(params.get("title"));
      newCollection.setCaption(params.get("caption"));
      newCollection.setImageUrl(imageUrl);
      newCollection.setCreatedAt(createdAt);
      newCollection.setUpdatedAt(updatedAt);

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

  // GET ALL COLLECTIONS ORDERED BY CREATED AT DESC
  public List<CollectionModel> getAllCollectionsByDesc() {
    List<CollectionModel> collections = repository.findAllByOrderByCreatedAtDesc();
    if (collections == null || collections.isEmpty()) {
      throw new CollectionCreationException("No collections found");
    }
    return collections;
  }

  // DELETE COLLECTION
  public void deleteCollection(Long collectionId) {
    try {
        // Validate and fetch the collection
        CollectionModel collection = getCollectionById(collectionId);
        if (collection == null) {
            throw new CollectionCreationException("Collection not found");
        }

        // Fetch and delete items in the collection
        List<ItemModel> items = itemService.getAllItemsByCollectionId(collectionId);
        if (items != null && !items.isEmpty()) {
            for (ItemModel item : items) {
                itemService.deleteItem(item.getId());
            }
        }

        // Delete the collection itself
        repository.delete(collection);
    } catch (NumberFormatException e) {
        throw new CollectionCreationException("Invalid collection ID", e);
    } catch (RuntimeException e) {
        throw new CollectionCreationException("Unexpected error occurred while deleting collection", e);
    }
}

  // UPDATE COLLECTION
  public Map<String, String> updateCollection(Map<String, String> params, MultipartFile collectionImage) {
    try {
      // Validate input parameters
      if (params.get("id") == null || params.get("id").isEmpty()) {
        throw new CollectionCreationException("Collection ID is required");
      }

      Long collectionId = Long.valueOf(params.get("id"));
      CollectionModel collection = getCollectionById(collectionId);
      if (collection == null) {
        throw new CollectionCreationException("Collection not found");
      }

      // Update collection properties
      if (params.get("title") != null && !params.get("title").isEmpty()) {
        collection.setTitle(params.get("title"));
      }
      if (params.get("caption") != null && !params.get("caption").isEmpty()) {
        collection.setCaption(params.get("caption"));
      }
      if (collectionImage != null && !collectionImage.isEmpty()) {
        String imageUrl = saveCollectionImage(collectionImage);
        collection.setImageUrl(imageUrl);
      }
      collection.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

      // Save the updated collection
      repository.save(collection);
      return Map.of("message", "Collection updated successfully");
    } catch (NumberFormatException e) {
      throw new CollectionCreationException("Invalid collection ID", e);
    } catch (RuntimeException e) {
      throw new CollectionCreationException("Unexpected error occurred while updating collection", e);
    }
  }



  // GET ALL COLLECTIONS WITH PARTITIONED ITEMS
  public List<CollectionModel> getCollectionsWithPartitionedItems() {
    List<CollectionModel> collections = repository.findAllByOrderByCreatedAtDesc();

    for (CollectionModel collection : collections) {
      // Fetch items for each collection
      List<ItemModel> items = itemService.getAllItemsByCollectionId(collection.getId());
      // Set the items in the collection
      collection.setItems(items);
    }

    return collections;
  }

  // GET ALL COLLECTIONS
  public List<CollectionModel> getAllCollections() {
    List<CollectionModel> collections = repository.findAllByOrderByCreatedAtDesc();
    if (collections == null || collections.isEmpty()) {
      throw new CollectionCreationException("No collections found");
    }
    return collections;
  }

  // GET COLLECTIONS BY USER ID
  public List<CollectionModel> getCollectionsByUserId(Long userId) {
    List<CollectionModel> collections = repository.findAllByUserIdOrderByCreatedAtDesc(userId);
    if (collections == null || collections.isEmpty()) {
      throw new CollectionCreationException("No collections found for user ID: " + userId);
    }
    return collections;
}

}
