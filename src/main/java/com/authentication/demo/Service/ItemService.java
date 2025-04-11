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
import com.authentication.demo.Model.ItemModel;
import com.authentication.demo.Repository.ItemRepository;

@Service
public class ItemService {

  private final ItemRepository itemRepository;
  private final UserService userService;

  public ItemService(ItemRepository itemRepository, UserService userService) {
    this.itemRepository = itemRepository;
    this.userService = userService;
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


    // COLLECTION ID
  final Long collectionId;
  if (params.get("collectionId") != null && !params.get("collectionId").isEmpty()) {
    try {
        collectionId = Long.valueOf(params.get("collectionId"));
    } catch (NumberFormatException e) {
        throw new ItemCreationException("Invalid collectionId format", e);
    }
  } else {
    throw new ItemCreationException("collectionId is required");
  }
    
    // CREATE ITEM
    ItemModel item = new ItemModel(
        null,
        userId,
        collectionId,
        params.get("title"),
        params.get("description"),
        params.get("itemLink"),
        params.get("caption"),
        imageUrl,
        new Timestamp(System.currentTimeMillis()),
        new Timestamp(System.currentTimeMillis())
    );

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

}
