
package com.authentication.demo.Controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.authentication.demo.Exceptions.ItemCreationException;
import com.authentication.demo.Service.ItemService;

@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(@Lazy ItemService itemService) {
        this.itemService = itemService;
    }

    // CREATE NEW ITEM
    @PostMapping("/create-item/{collectionId}")
    public String createItem(
            @PathVariable Long collectionId,
            @RequestParam Map<String, String> params,
            @RequestParam MultipartFile itemImage,
            RedirectAttributes redirectAttributes) {
        try {
            params.put("collectionId", String.valueOf(collectionId));
            itemService.createItem(params, itemImage);
            redirectAttributes.addFlashAttribute("message", "Item created successfully");
            return "redirect:/collection/" + collectionId;
        } catch (ItemCreationException | IOException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/create-item/" + collectionId;
        }
    }

    @PostMapping("/delete-item/{id}")
    public String deleteItem(@PathVariable("id") Long itemId, RedirectAttributes redirectAttributes) {
        try {
            // Fetch the item to get its collectionId
            Long collectionId = itemService.getCollectionIdByItemId(itemId);

            // Delete the item
            itemService.deleteItem(itemId);

            // Add success message
            redirectAttributes.addFlashAttribute("message", "Item deleted successfully.");

            // Redirect to the collection page
            return "redirect:/collection/" + collectionId;
        } catch (ItemCreationException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete item: " + e.getMessage());
            return "redirect:/update-item/" + itemId;
        }
    }

    // UPDATE ITEM
    @PostMapping("/update-item/{id}")
    public String updateItem(
            @PathVariable("id") Long itemId,
            @RequestParam Map<String, String> params,
            @RequestParam(required = false) MultipartFile itemImage,
            RedirectAttributes redirectAttributes) throws IOException {
        try {
            params.put("itemId", String.valueOf(itemId));
            itemService.updateItem(params, itemImage);
            redirectAttributes.addFlashAttribute("message", "Item updated successfully");
        } catch (ItemCreationException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update item: " + e.getMessage());
            return "redirect:/update-item/" + itemId;
        }
        return "redirect:/item/" + itemId;
    }

}