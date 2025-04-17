package com.authentication.demo.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.authentication.demo.Exceptions.ItemCreationException;
import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Model.ItemModel;
import com.authentication.demo.Repository.CollectionRepository;
import com.authentication.demo.Repository.ItemRepository;

@Service
public class ItemService {

  private final ItemRepository itemRepository;
  private final UserService userService;
  private final CollectionRepository collectionRepository;

  public ItemService(ItemRepository itemRepository, UserService userService,
      CollectionRepository collectionRepository) {
    this.itemRepository = itemRepository;
    this.userService = userService;
    this.collectionRepository = collectionRepository;
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
  public Map<String, String> createItem(Map<String, String> params, MultipartFile collectionImage) {
    // VALIDATE INPUT PARAMETERS

    if (collectionImage == null || collectionImage.isEmpty()) {
      throw new ItemCreationException("Item image is required");
    }

    // GET USER ID
    Long userId = userService.getCurrentUserId();

    // SAVE ITEM IMAGE
    String imageUrl = saveItemImage(collectionImage);

    // GET COLLECTION
    Long collectionId = Long.valueOf(params.get("collectionId"));
    CollectionModel collection = collectionRepository.findById(collectionId)
        .orElseThrow(() -> new ItemCreationException("Collection not found"));
    if (collection == null) {
      throw new ItemCreationException("Collection not found");
    }

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

  // SAVE PROFILE PICTURE
  public String saveItemImage(MultipartFile itemImage) {
    // CREATE ITEM IMAGE DIRECTORY
    File directory = new File("item-images");
    if (!directory.exists()) {
      directory.mkdirs();
    }

    // ITEM FILE DIRECTORY EXISTS?
    File itemImageDir = new File("item-images");
    if (!itemImageDir.exists()) {
      itemImageDir.mkdirs();
    }

    // GENERATE A UNIQUE FILENAME
    String filename = UUID.randomUUID().toString() + "-" + itemImage.getOriginalFilename();
    File file = new File(directory, filename);

    // SAVE ITEM IMAGE
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write(itemImage.getBytes());
    } catch (Exception e) {
      throw new RuntimeException("Failed to save item image", e);
    }
    // RETURN IMAGE URL
    return "/item-images/" + filename;
  }

  public List<ItemModel> getAllItemsByCollectionId(Long collectionId) {
    System.out.println("Fetching items for collectionId: " + collectionId);
    List<ItemModel> items = itemRepository.findAllByCollectionIdOrderByCreatedAtDesc(collectionId);
    System.out.println("Items fetched: " + (items != null ? items.size() : "null"));
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
      String imageUrl = saveItemImage(itemImage);
      item.setImageUrl(imageUrl);
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

}
