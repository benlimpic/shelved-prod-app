package com.authentication.demo.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.authentication.demo.Exceptions.ItemCreationException;
import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Model.CommentModel;
import com.authentication.demo.Model.ItemModel;
import com.authentication.demo.Repository.CollectionRepository;
import com.authentication.demo.Repository.CommentRepository;
import com.authentication.demo.Repository.ItemRepository;

@Service
public class ItemService {
  private final ItemRepository itemRepository;
  private final UserService userService;
  private final CollectionRepository collectionRepository;
  private final CommentRepository commentRepository;
  private final S3Service s3Service;
  private final ImageService imageService;

  public ItemService(
      ItemRepository itemRepository,
      UserService userService,
      CollectionRepository collectionRepository,
      CommentRepository commentRepository,
      S3Service s3Service,
      ImageService imageService) {
    this.itemRepository = itemRepository;
    this.userService = userService;
    this.collectionRepository = collectionRepository;
    this.commentRepository = commentRepository;
    this.s3Service = s3Service;
    this.imageService = imageService;
  }

  // GET ITEM BY ITEM ID
  public ItemModel getItemById(Long id) {
    ItemModel item = itemRepository.findById(id).orElse(null);
    if (item == null) {
      throw new ItemCreationException("Item not found");
    }
    return item;
  }

  // CREATE ITEM
  public Map<String, String> createItem(Map<String, String> params, MultipartFile itemImage) throws IOException {
    // VALIDATE INPUT PARAMETERS
    if (itemImage == null || itemImage.isEmpty()) {
      throw new ItemCreationException("Item image is required");
    }

    // GET USER ID
    Long userId = userService.getCurrentUserId();

    // PROCESS ITEM IMAGE
    MultipartFile processedFile;
    try {
      processedFile = imageService.processImage(itemImage);
    } catch (IOException e) {
      throw new ItemCreationException("Failed to process the item image", e);
    }

    // SAVE ITEM IMAGE
    String imageUrl = saveItemImage(processedFile);

    // GET COLLECTION
    Long collectionId = Long.valueOf(params.get("collectionId"));
    CollectionModel collection = collectionRepository.findById(collectionId)
        .orElseThrow(() -> new ItemCreationException("Collection not found"));

    // CREATE ITEM
    ItemModel item = new ItemModel(
        null,
        userId,
        collection,
        params.get("title"),
        params.get("description"),
        params.get("itemLink"),
        params.get("caption"),
        imageUrl,
        new Timestamp(System.currentTimeMillis()),
        new Timestamp(System.currentTimeMillis()));

    // SAVE ITEM TO DATABASE
    itemRepository.save(item);

    // RETURN SUCCESS RESPONSE
    return Map.of("message", "Item created successfully", "itemId", item.getId().toString());
  }

  // SAVE COLLECTION IMAGE
  public String saveItemImage(MultipartFile itemImage) throws IOException {
    String bucketName = "shelved-item-images-benlimpic";
    String filename = UUID.randomUUID().toString() + "-" + itemImage.getOriginalFilename();

    // Validate the file
    if (itemImage.isEmpty()) {
      throw new IllegalArgumentException("Profile picture file is empty");
    }

    // Upload the file to S3
    try {
      System.out.println("Starting file upload to S3...");
      File tempFile = File.createTempFile("upload-", itemImage.getOriginalFilename());
      itemImage.transferTo(tempFile);
      try (InputStream inputStream = new FileInputStream(tempFile)) {
        String contentType = itemImage.getContentType(); // Get content type from MultipartFile
        s3Service.uploadFile(bucketName, filename, inputStream, contentType);
      }
      tempFile.deleteOnExit();
      System.out.println("File uploaded to S3 successfully.");

      // Generate a presigned URL
      String fileUrl = s3Service.generatePresignedUrl(bucketName, filename);
      System.out.println("Generated S3 URL: " + fileUrl);

      return fileUrl;
    } catch (Exception e) {
      System.err.println("Error in saveProfilePicture: " + e.getMessage());
      e.printStackTrace();
      throw new RuntimeException("Failed to upload profile picture to S3", e);
    }
  }

  public List<ItemModel> getAllItemsByCollectionId(Long collectionId) {

    List<ItemModel> items = itemRepository.findAllByCollectionIdOrderByCreatedAtDesc(collectionId);
    return items;
  }

  // DELETE ITEM
  public void deleteItem(Long itemId) {
    // VALIDATE INPUT PARAMETERS
    if (itemId == null) {
      throw new ItemCreationException("Item ID is required");
    }

    // CHECK IF ITEM EXISTS
    if (!itemRepository.existsById(itemId)) {
      throw new ItemCreationException("Item not found");
    }

    // DELETE ITEM
    itemRepository.deleteById(itemId);
  }

  // UPDATE ITEM
  public Map<String, String> updateItem(Map<String, String> params, MultipartFile itemImage) {
    // Validate input parameters
    if (params.get("itemId") == null || params.get("itemId").isEmpty()) {
      throw new ItemCreationException("Item ID is required");
    }

    // Get item by ID
    Long itemId;
    try {
      itemId = Long.valueOf(params.get("itemId"));
    } catch (NumberFormatException e) {
      throw new ItemCreationException("Invalid Item ID format");
    }
    ItemModel item = getItemById(itemId);

    // Update item fields
    if (params.get("title") != null && !params.get("title").isEmpty()) {
      item.setTitle(params.get("title"));
    }
    if (params.get("description") != null && !params.get("description").isEmpty()) {
      item.setDescription(params.get("description"));
    }
    if (params.get("itemLink") != null && !params.get("itemLink").isEmpty()) {
      item.setItemLink(params.get("itemLink"));
    }
    if (params.get("caption") != null && !params.get("caption").isEmpty()) {
      item.setCaption(params.get("caption"));
    }
    if (itemImage != null && !itemImage.isEmpty()) {
      try {
        MultipartFile processedFile = imageService.processImage(itemImage);
        String imageUrl = saveItemImage(processedFile);
        item.setImageUrl(imageUrl);
      } catch (IOException e) {
        throw new ItemCreationException("Failed to save item image: " + e.getMessage());
      }
    }

    // Save the updated item
    itemRepository.save(item);

    // Return success response
    return Map.of("message", "Item updated successfully");
  }

  public Long getCollectionIdByItemId(Long itemId) {
    ItemModel item = itemRepository.findById(itemId).orElse(null);
    if (item == null) {
      throw new ItemCreationException("Item not found");
    }
    return item.getCollection().getId();
  }

  public List<CommentModel> getCommentsByItemIdDesc(Long itemId) {
    return commentRepository.findByItemIdOrderByCreatedAtDesc(itemId);
  }

  public Integer countComments(Long itemId) {
    return commentRepository.countByItemId(itemId);
  }

}
