package com.authentication.demo.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Model.CommentModel;
import com.authentication.demo.Model.ItemModel;
import com.authentication.demo.Model.LikeModel;
import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.CollectionRepository;
import com.authentication.demo.Repository.CommentRepository;
import com.authentication.demo.Repository.ItemRepository;
import com.authentication.demo.Repository.LikeRepository;
import com.authentication.demo.Repository.ReplyRepository;
import com.authentication.demo.Repository.UserRepository;

@Controller
public class LikeController {

  private final LikeRepository likeRepository;
  private final UserRepository userRepository;
  private final CollectionRepository collectionRepository;
  private final CommentRepository commentRepository;
  private final ItemRepository itemRepository;
  private final ReplyRepository replyRepository;

  public LikeController(
    LikeRepository likeRepository,
    UserRepository userRepository,
    CollectionRepository collectionRepository,
    CommentRepository commentRepository,
    ItemRepository itemRepository,
    ReplyRepository replyRepository) {
    this.likeRepository = likeRepository;
    this.userRepository = userRepository;
    this.collectionRepository = collectionRepository;
    this.commentRepository = commentRepository;
    this.itemRepository = itemRepository;
    this.replyRepository = replyRepository;
  }

  @PostMapping("/collections/{collectionId}/like-from-index")
  public String toggleLikeFromIndex(@PathVariable Long collectionId, @RequestParam long userId) {
    UserModel currentUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    CollectionModel collection = collectionRepository.findById(collectionId)
        .orElseThrow(() -> new RuntimeException("Collection not found"));

    // Process the like/unlike logic
    List<LikeModel> existingLikes = likeRepository.findAllByCollectionId(collectionId);
    boolean alreadyLiked = existingLikes.stream()
        .anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));
    if (alreadyLiked) {
      LikeModel existingLike = existingLikes.stream()
          .filter(like -> like.getUser().getId().equals(currentUser.getId()))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Like not found"));
      likeRepository.delete(existingLike);
    } else {
      LikeModel newLike = new LikeModel();
      newLike.setUser(currentUser);
      newLike.setCollection(collection);
      likeRepository.save(newLike);
    }

    // Redirect back to the index page with the hash
    return "redirect:/index#collection-" + collectionId;
  }

  @PostMapping("/collections/{collectionId}/like")
  public String toggleLike(@PathVariable Long collectionId, long userId) {
    UserModel currentUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    CollectionModel collection = collectionRepository.findById(collectionId)
        .orElseThrow(() -> new RuntimeException("Collection not found"));
    List<LikeModel> existingLikes = likeRepository.findAllByCollectionId(collectionId);
    boolean alreadyLiked = existingLikes.stream()
        .anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));
    if (alreadyLiked) {
      // If the user already liked the collection, remove the like
      LikeModel existingLike = existingLikes.stream()
          .filter(like -> like.getUser().getId().equals(currentUser.getId()))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Like not found"));
      likeRepository.delete(existingLike);
    } else {
      // If the user has not liked the collection, add a new like
      LikeModel newLike = new LikeModel();
      newLike.setUser(currentUser);
      newLike.setCollection(collection);
      likeRepository.save(newLike);
    }

    return "redirect:/collection/" + collectionId + "#collection-" + collectionId;
  }

  @PostMapping("/items/{itemId}/like")
  public String toggleLikeItem(@PathVariable Long itemId, long userId) {
    UserModel currentUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    ItemModel item = itemRepository.findById(itemId)
        .orElseThrow(() -> new RuntimeException("Collection not found"));
    List<LikeModel> existingLikes = likeRepository.findAllByItemId(itemId);
    boolean alreadyLiked = existingLikes.stream()
        .anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));
    if (alreadyLiked) {
      // If the user already liked the collection, remove the like
      LikeModel existingLike = existingLikes.stream()
          .filter(like -> like.getUser().getId().equals(currentUser.getId()))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Like not found"));
      likeRepository.delete(existingLike);
    } else {
      // If the user has not liked the collection, add a new like
      LikeModel newLike = new LikeModel();
      newLike.setUser(currentUser);
      newLike.setItem(item);
      likeRepository.save(newLike);
    }
    return "redirect:/item/" + itemId;
  }

  @PostMapping("/comments/{commentId}/like")
  public String toggleLikeComment(@PathVariable Long commentId, long userId) {
    UserModel currentUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    CommentModel comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new RuntimeException("Collection not found"));
    List<LikeModel> existingLikes = likeRepository.findAllByCommentId(commentId);
    boolean alreadyLiked = existingLikes.stream()
        .anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));
    if (alreadyLiked) {
      // If the user already liked the collection, remove the like
      LikeModel existingLike = existingLikes.stream()
          .filter(like -> like.getUser().getId().equals(currentUser.getId()))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Like not found"));
      likeRepository.delete(existingLike);
    } else {
      // If the user has not liked the collection, add a new like
      LikeModel newLike = new LikeModel();
      newLike.setUser(currentUser);
      newLike.setComment(comment);
      likeRepository.save(newLike);
    }

    if (comment.getCollection() != null) {
      Long collectionId = comment.getCollection().getId();
      return "redirect:/collection/" + collectionId + "/comments#comment-" + commentId;
    } else if (comment.getItem() != null) {
      Long itemId = comment.getItem().getId();
      return "redirect:/item/" + itemId + "/comments#comment-" + commentId;
    } else {
      throw new RuntimeException("Comment does not belong to a collection or item");
    }
  }

  @PostMapping("/replies/{replyId}/like")
  public String toggleLikeReply(@PathVariable Long replyId, Principal principal) {
    String username = principal.getName();
    UserModel currentUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

    List<LikeModel> existingLikes = likeRepository.findAllByReplyId(replyId);
    boolean alreadyLiked = existingLikes.stream()
        .anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));
    if (alreadyLiked) {
      // If the user already liked the reply, remove the like
      LikeModel existingLike = existingLikes.stream()
          .filter(like -> like.getUser().getId().equals(currentUser.getId()))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Like not found"));
      likeRepository.delete(existingLike);
    } else {
      // If the user has not liked the reply, add a new like
      LikeModel newLike = new LikeModel();
      newLike.setUser(currentUser);
      newLike.setReply(replyRepository.findById(replyId)
          .orElseThrow(() -> new RuntimeException("Reply not found")));
      likeRepository.save(newLike);
    }

    CommentModel comment = replyRepository.findById(replyId).get().getComment();

    if (comment.getCollection() != null) {
      Long collectionId = comment.getCollection().getId();
      return "redirect:/collection/" + collectionId + "/comments#reply-" + replyId;
    } else if (comment.getItem() != null) {
      Long itemId = comment.getItem().getId();
      return "redirect:/item/" + itemId + "/comments#reply-" + replyId;
    } else {
      throw new RuntimeException("Comment does not belong to a collection or item");
    }

  }

}
