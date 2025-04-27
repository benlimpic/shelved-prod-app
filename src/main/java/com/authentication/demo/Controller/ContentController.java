package com.authentication.demo.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Model.CommentModel;
import com.authentication.demo.Model.ItemModel;
import com.authentication.demo.Model.LikeModel;
import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.LikeRepository;
import com.authentication.demo.Repository.UserRepository;
import com.authentication.demo.Service.CollectionService;
import com.authentication.demo.Service.FollowService;
import com.authentication.demo.Service.ItemService;
import com.authentication.demo.Service.LikeService;
import com.authentication.demo.Service.UserService;

@Controller
public class ContentController {

    private final UserRepository repository;
    private final UserService userService;
    private final CollectionService collectionService;
    private final ItemService itemService;
    private final FollowService followService;
    private final LikeService likeService;
    private final LikeRepository likeRepository;

    public ContentController(UserRepository repository, UserService userService, CollectionService collectionService,
            ItemService itemService, FollowService followService, LikeService likeService,
            LikeRepository likeRepository) {
        this.repository = repository;
        this.userService = userService;
        this.collectionService = collectionService;
        this.itemService = itemService;
        this.followService = followService;
        this.likeService = likeService;
        this.likeRepository = likeRepository;
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

        // Fetch current user
        UserModel currentUser = userService.getCurrentUser().orElse(null);
        if (currentUser == null) {
            return "redirect:/login"; // Redirect to login if user is not authenticated
        }

        // Fetch Users
        Map<Long, UserModel> users = userService.getUsersMappedById();
        model.addAttribute("users", users);

        // Fetch collections by following

        List<CollectionModel> collections = collectionService.getCollectionsByFollowing(currentUser.getId());

        // Partition items for each collection
        Map<Long, List<List<ItemModel>>> partitionedItemsByCollection = new HashMap<>();
        for (CollectionModel collection : collections) {
            // Fetch the collection owner
            UserModel collectionOwner = userService.getUserById(collection.getUser().getId());
            if (collectionOwner != null) {
                collection.setUser(collectionOwner); // Set the owner in the collection
            }

            // Fetch the number of likes for the collection
            Integer likeCount = likeService.countLikes(collection.getId());
            collection.setLikeCount(likeCount);

            //NUMBER OF COMMENTS
            Integer commentCount = collectionService.countComments(collection.getId());
            collection.setCommentCount(commentCount);


            // REVERSE ORDER COMMENTS
            List<CommentModel> comments = collectionService.getCommentsByCollectionIdDesc(collection.getId());
            collection.setComments(comments);

            // Fetch the isLiked status for the collection
            List<LikeModel> likes = likeRepository.findAllByCollectionId(collection.getId());
            boolean isLiked = likes.stream().anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));
            collection.setIsLiked(isLiked);

            for (CommentModel comment : collection.getComments()) {
                UserModel commentOwner = userService.getUserById(comment.getUser().getId());
                if (commentOwner != null) {
                    comment.setUser(commentOwner); // Set the owner in the comment
                }
                // Fetch the number of likes for the comment
                Integer commentLikeCount = likeService.countLikesComment(comment.getId());
                comment.setLikeCount(commentLikeCount);
                // Fetch the isLiked status for the comment
                List<LikeModel> commentLikes = likeRepository.findAllByCommentId(comment.getId());
                boolean isCommentLiked = commentLikes.stream().anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));
                comment.setIsLiked(isCommentLiked);
            }


            // Fetch items for each collection
            List<ItemModel> items = itemService.getAllItemsByCollectionId(collection.getId());
            List<List<ItemModel>> partitionedItems = items != null ? ListUtils.partition(items, 3) : new ArrayList<>();
            partitionedItemsByCollection.put(collection.getId(), partitionedItems);
        }

        

        // Add data to the model
        model.addAttribute("collections", collections);
        model.addAttribute("partitionedItemsByCollection", partitionedItemsByCollection);
        model.addAttribute("users", users);

        return handleAuthentication(model, "index");
    }

    @GetMapping("/profile")
    public String profile(Model model) {

        // FETCH CURRENT USER
        UserModel currentUser = userService.getCurrentUser().orElse(null);
        if (currentUser == null) {
            return "redirect:/login"; // Redirect to login if user is not authenticated
        }

        // FETCH SORTED COLLECTIONS BY USER ID
        List<CollectionModel> collections = collectionService.getCollectionsForCurrentUser();

        // BREAK COLLECTIONS INTO GROUPS OF 3
        List<List<CollectionModel>> partitionedCollections = ListUtils.partition(collections, 3);

        // COUNT COLLECTIONS
        int collectionCount = collections.size();

        int followingCount = followService.countByFollower(currentUser);
        int followersCount = followService.countByFollowed(currentUser);

        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followersCount", followersCount);

        // ADD TO MODEL
        model.addAttribute("collectionCount", collectionCount);
        model.addAttribute("partitionedCollections", partitionedCollections);
        return handleAuthentication(model, "profile");
    }

    @GetMapping("/profile/{id}")
    public String userProfile(@PathVariable("id") Long userId, Model model) {

        UserModel currentUser = userService.getCurrentUser().orElse(null);
        UserModel userProfile = userService.getUserById(userId);

        Boolean isFollwing = followService.isFollowing(currentUser, userProfile);

        // Fetch the user profile
        if (userProfile == null) {
            return "redirect:/index"; // Redirect to index if user profile is not found
        } else if (userProfile.getId().equals(userService.getCurrentUserId())) {
            return "redirect:/profile"; // Redirect to own profile
        }

        int followingCount = followService.countByFollower(userProfile);
        int followersCount = followService.countByFollowed(userProfile);

        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followersCount", followersCount);

        // Fetch collections for the user
        List<CollectionModel> collections = collectionService.getCollectionsByUserId(userId);
        // Partition collections into rows of 3 for display
        List<List<CollectionModel>> partitionedCollections = ListUtils.partition(collections, 3);
        // Count Collections
        int collectionCount = collections.size();
        // Add the attributes to model
        model.addAttribute("isFollowing", isFollwing);
        model.addAttribute("collectionCount", collectionCount);
        model.addAttribute("partitionedCollections", partitionedCollections);
        model.addAttribute("userProfile", userProfile);

        return handleAuthentication(model, "userProfile");
    }

    @GetMapping("/profile/{id}/following")
    public String getUserFollowing(@PathVariable Long id, Model model) {
        UserModel userProfile = userService.getUserById(id);
        if (userProfile == null) {
            return "redirect:/index"; // Handle missing user profile
        }

        List<UserModel> followingList = followService.getFollowing(userProfile);
        model.addAttribute("followingList", followingList);

        model.addAttribute("userProfile", userProfile);

        return handleAuthentication(model, "followingList");
    }

    @GetMapping("/profile/{id}/followers")
    public String getUserFollowers(@PathVariable Long id, Model model) {
        UserModel userProfile = userService.getUserById(id);
        if (userProfile == null) {
            return "redirect:/index"; // Handle missing user profile
        }

        List<UserModel> followersList = followService.getFollowers(userProfile);
        model.addAttribute("followersList", followersList);

        model.addAttribute("userProfile", userProfile);

        return handleAuthentication(model, "followersList");
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

        // Fetch current user
        UserModel currentUser = userService.getCurrentUser().orElse(null);
        if (currentUser == null) {
            return "redirect:/login"; // Redirect to login if user is not authenticated
        }

        // Fetch the collection by ID
        CollectionModel collection = collectionService.getCollectionById(collectionId);
        if (collection == null) {
            return "redirect:/profile"; // Redirect if the collection is not found
        }

        // Fetch the collection owner by ID
        UserModel userProfile = userService.getUserById(collection.getUser().getId());
        if (userProfile == null) {
            return "redirect:/profile"; // Redirect if the collection owner is not found
        }

        // Fetch items for the collection
        List<ItemModel> items = itemService.getAllItemsByCollectionId(collectionId);

        // Partition items into rows of 3 for display
        List<List<ItemModel>> partitionedItems = items != null ? ListUtils.partition(items, 3) : List.of();

        // Is the current user the owner of the collection?
        boolean isOwner = collection.getUser().getId().equals(userService.getCurrentUserId());

        // Fetch the number of likes for the collection
        Integer likeCount = likeService.countLikes(collectionId);
        
        // Fetch the isLiked status for the collection

        List<LikeModel> likes = likeRepository.findAllByCollectionId(collectionId);
        boolean isLiked = likes.stream().anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));
        
        collection.setComments(collectionService.getCommentsByCollectionIdDesc(collectionId));

        for (CommentModel comment : collection.getComments()) {
            UserModel commentOwner = userService.getUserById(comment.getUser().getId());
            if (commentOwner != null) {
                comment.setUser(commentOwner); // Set the owner in the comment
            }
            // Fetch the number of likes for the comment
            Integer commentLikeCount = likeService.countLikesComment(comment.getId());
            comment.setLikeCount(commentLikeCount);
            // Fetch the isLiked status for the comment
            List<LikeModel> commentLikes = likeRepository.findAllByCommentId(comment.getId());
            boolean isCommentLiked = commentLikes.stream().anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));
            comment.setIsLiked(isCommentLiked);
        }

        Integer commentCount = collectionService.countComments(collectionId);
        collection.setCommentCount(commentCount);


        // Add data to the model
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("userProfile", userProfile);
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

        // Fetch current user
        UserModel currentUser = userService.getCurrentUser().orElse(null);
        if (currentUser == null) {
            return "redirect:/login"; // Redirect to login if user is not authenticated
        }

        // Fetch the item by ID
        ItemModel item = itemService.getItemById(itemId);
        if (item == null) {
            return "redirect:/profile"; // Redirect if the item is not found
        }

        // Fetch the collection associated with the item
        CollectionModel collection = item.getCollection();
        if (collection == null) {
            return "redirect:/profile"; // Redirect if the collection is not found
        }

        // Is the current user the owner of the collection?
        boolean isOwner = item.getUserId().equals(userService.getCurrentUserId());

        // Fetch the number of likes for the collection
        Integer likeCount = likeService.countLikesItem(itemId);
        
        // Fetch the isLiked status for the collection

        List<LikeModel> likes = likeRepository.findAllByItemId(itemId);
        boolean isLiked = likes.stream().anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));

        item.setComments(itemService.getCommentsByItemIdDesc(itemId));

        for (CommentModel comment : item.getComments()) {
            UserModel commentOwner = userService.getUserById(comment.getUser().getId());
            if (commentOwner != null) {
                comment.setUser(commentOwner); // Set the owner in the comment
            }
            // Fetch the number of likes for the comment
            Integer commentLikeCount = likeService.countLikesComment(comment.getId());
            comment.setLikeCount(commentLikeCount);
            // Fetch the isLiked status for the comment
            List<LikeModel> commentLikes = likeRepository.findAllByCommentId(comment.getId());
            boolean isCommentLiked = commentLikes.stream().anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));
            comment.setIsLiked(isCommentLiked);
        }

        Integer commentCount = itemService.countComments(itemId);
        item.setCommentCount(commentCount);

        // Add the item and collection to the model

        model.addAttribute("likeCount", likeCount);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("item", item);
        model.addAttribute("collection", collection);

        return handleAuthentication(model, "item");
    }

    @GetMapping("/update-item/{id}")
    public String updateItem(@PathVariable("id") Long itemId, Model model) {
        ItemModel item = itemService.getItemById(itemId);
        if (item == null) {
            return "redirect:/profile"; // Handle missing item
        }
        model.addAttribute("item", item);
        return handleAuthentication(model, "updateItem");
    }

    @GetMapping("/update-collection/{id}")
    public String updateCollection(@PathVariable("id") Long collectionId, Model model) {
        // Fetch the collection by ID
        CollectionModel collection = collectionService.getCollectionById(collectionId);
        if (collection == null) {
            return "redirect:/profile"; // Redirect if the collection is not found
        }

        // Add the collection to the model
        model.addAttribute("collection", collection);

        return handleAuthentication(model, "updateCollection");
    }

    @GetMapping("/popular")
    public String popular(Model model) {
        // Fetch current user
        UserModel currentUser = userService.getCurrentUser().orElse(null);
        if (currentUser == null) {
            return "redirect:/login"; // Redirect to login if user is not authenticated
        }

        // Fetch collections for the user
        List<CollectionModel> collections = collectionService.getAllCollectionsByDesc();
        // Partition collections into rows of 3 for display
        List<List<CollectionModel>> partitionedCollections = ListUtils.partition(collections, 3);

        // Add data to the model
        model.addAttribute("collections", collections);
        model.addAttribute("partitionedCollections", partitionedCollections);

        return handleAuthentication(model, "popular");
    }

    private String handleAuthentication(Model model, String viewName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null && !authentication.getName().isEmpty()) {
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
