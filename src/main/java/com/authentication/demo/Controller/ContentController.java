package com.authentication.demo.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Model.ItemModel;
import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.UserRepository;
import com.authentication.demo.Service.CollectionService;
import com.authentication.demo.Service.ItemService;
import com.authentication.demo.Service.UserService;

@Controller
public class ContentController {

    private final UserRepository repository;
    private final UserService userService;
    private final CollectionService collectionService;
    private final ItemService itemService;

    public ContentController(UserRepository repository, UserService userService, CollectionService collectionService,
            ItemService itemService) {
        this.repository = repository;
        this.userService = userService;
        this.collectionService = collectionService;
        this.itemService = itemService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<CollectionModel> collections = collectionService.getAllCollectionsByDesc();
        model.addAttribute("collections", collections);
    
        // Fetch user details for all collections
        Map<Long, UserModel> users = userService.getUsersByIds(
            collections.stream().map(CollectionModel::getUserId).collect(Collectors.toList())
        );
        model.addAttribute("users", users);

        return handleAuthentication(model, "index");
        
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        // FETCH SORTED COLLECTIONS BY USER ID
        List<CollectionModel> collections = collectionService.getCollectionsForCurrentUser();

        // BREAK COLLECTIONS INTO GROUPS OF 3
        List<List<CollectionModel>> partitionedCollections = ListUtils.partition(collections, 3);

        // ADD TO MODEL
        model.addAttribute("partitionedCollections", partitionedCollections);
        return handleAuthentication(model, "profile");
    }

    @GetMapping("/update-profile")
    public String updateProfile(Model model) {
        return handleAuthentication(model, "updateProfile");
    }

    @GetMapping("/update-user-details")
    public String updateUserDetails(Model model) {
        return handleAuthentication(model, "updateUserDetails");
    }

    @GetMapping("/create-collection")
    public String createCollection(Model model) {
        return handleAuthentication(model, "createCollection");
    }

    @GetMapping("/collection/{id}")
    public String collection(@PathVariable("id") Long collectionId, Model model) {
        // Fetch the collection by ID
        CollectionModel collection = collectionService.getCollectionById(collectionId);
        if (collection == null) {
            return "redirect:/profile"; // Redirect if the collection is not found
        }

        // Fetch items for the collection
        List<ItemModel> items = itemService.getAllItemsByCollectionId(collectionId);

        // Partition items into rows of 3 for display
        List<List<ItemModel>> partitionedItems = items != null ? ListUtils.partition(items, 3) : List.of();

        // Add data to the model
        model.addAttribute("collection", collection);
        model.addAttribute("partitionedItems", partitionedItems);

        return handleAuthentication(model, "collection");
    }

    @GetMapping("/create-item/{collectionId}")
    public String createItem(@PathVariable("collectionId") Long collectionId, Model model) {
        // Add the collectionId to the model
        model.addAttribute("collectionId", collectionId);
        return handleAuthentication(model, "createItem");
    }

    @GetMapping("/item/{id}")
    public String getItem(@PathVariable("id") Long itemId, Model model) {
        // Fetch the item by ID
        ItemModel item = itemService.getItemById(itemId);
        if (item == null) {
            return "redirect:/profile"; // Redirect if the item is not found
        }

        // Fetch the collection associated with the item
        CollectionModel collection = collectionService.getCollectionById(item.getCollectionId());
        if (collection == null) {
            return "redirect:/profile"; // Redirect if the collection is not found
        }

        // Add the item and collection to the model
        model.addAttribute("item", item);
        model.addAttribute("collection", collection);

        return handleAuthentication(model, "item");
    }

    private String handleAuthentication(Model model, String viewName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            Optional<UserModel> user = repository.findByUsername(authentication.getName());
            if (user.isPresent()) {
                UserModel userModel = user.get();
                model.addAttribute("user", userModel);
                return viewName;
            }
        }
        return "redirect:/login";
    }

}
