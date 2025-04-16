
package com.authentication.demo.Controller;

import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.authentication.demo.Service.ItemService;

@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(@Lazy ItemService itemService) {
        this.itemService = itemService;
    }

    // CREATE NEW ITEM
    @PostMapping("/create_item/{collectionId}")
    public String createItem(
            @PathVariable Long collectionId,
            @RequestParam Map<String, String> params,
            @RequestParam("itemImage") MultipartFile itemImage,
            RedirectAttributes redirectAttributes) {
        itemService.createItem(params, itemImage);
        redirectAttributes.addFlashAttribute("message", "Item created successfully");
        return "redirect:/collection/" + collectionId;
    }

    // DELETE ITEM
    @PostMapping("/delete_item/{id}")
    public String deleteItem(
            @PathVariable Long id,
            @RequestParam Map<String, String> params,
            RedirectAttributes redirectAttributes) {
        itemService.deleteItem(id);
        redirectAttributes.addFlashAttribute("message", "Item deleted successfully");
        return "redirect:/profile";
    }

    // UPDATE ITEM
    @PostMapping("/update_item/{id}")
    public String updateItem(
            @PathVariable Long id,
            @RequestParam Map<String, String> params,
            @RequestParam("itemImage") MultipartFile itemImage,
            RedirectAttributes redirectAttributes) {
        itemService.updateItem(params, itemImage);
        redirectAttributes.addFlashAttribute("message", "Item updated successfully");
        return "redirect:/profile";
    }

}