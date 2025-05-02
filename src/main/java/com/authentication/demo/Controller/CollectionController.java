package com.authentication.demo.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.authentication.demo.Exceptions.CollectionCreationException;
import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Repository.CollectionRepository;
import com.authentication.demo.Service.CollectionService;

@Controller
public class CollectionController {

    private final CollectionService collectionService;
    private final CollectionRepository collectionRepository;

    public CollectionController(@Lazy CollectionService collectionService, CollectionRepository collectionRepository) {
        this.collectionService = collectionService;
        this.collectionRepository = collectionRepository;
    }

    // CREATE NEW COLLECTION
    @PostMapping("/create_collection")
    public String postCollection(@RequestParam Map<String, String> collectionDetails,
            @RequestParam MultipartFile collectionImage,
            RedirectAttributes redirectAttributes) {
        collectionService.createCollection(collectionDetails, collectionImage);
        redirectAttributes.addFlashAttribute("message", "Collection created successfully");
        return "redirect:/profile";
    }

    @PostMapping("/delete-collection/{id}")
    public String deleteCollection(@PathVariable("id") Long collectionId, RedirectAttributes redirectAttributes) {
        try {
            collectionService.deleteCollection(collectionId);
            redirectAttributes.addFlashAttribute("message", "Collection deleted successfully.");
        } catch (CollectionCreationException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete collection: " + e.getMessage());
            return "redirect:/update-collection/" + collectionId;
        }
        return "redirect:/profile";
    }

    // UPDATE COLLECTION
    @PostMapping("/update-collection/{id}")
    public String updateCollection(
            @PathVariable("id") Long collectionId,
            @RequestParam Map<String, String> params,
            @RequestParam(required = false) MultipartFile collectionImage,
            RedirectAttributes redirectAttributes) {
        try {
            // Add the collection ID to the params map
            params.put("id", String.valueOf(collectionId));

            // Call the service to update the collection
            collectionService.updateCollection(params, collectionImage);
            redirectAttributes.addFlashAttribute("message", "Collection updated successfully");
        } catch (CollectionCreationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/update-collection/" + collectionId;
        }
        return "redirect:/collection/" + collectionId;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> searchCollections(@RequestParam("query") String query) {
        List<CollectionModel> collections = collectionRepository.findByTitleContainingIgnoreCase(query);
        Map<String, Object> response = new HashMap<>();
        response.put("collections", collections);
        return ResponseEntity.ok(response);
    }

}
