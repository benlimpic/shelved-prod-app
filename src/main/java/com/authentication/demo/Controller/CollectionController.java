package com.authentication.demo.Controller;

import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.authentication.demo.Service.CollectionService;

@Controller
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(@Lazy CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    // CREATE NEW COLLECTION
    @PostMapping("/create_collection")
    public String postCollection(@RequestParam Map<String, String> collectionDetails, @RequestParam MultipartFile collectionImage,
        RedirectAttributes redirectAttributes) {
        collectionService.createCollection(collectionDetails, collectionImage);
        redirectAttributes.addFlashAttribute("message", "Collection created successfully");
        return "redirect:/profile";
    }


}
