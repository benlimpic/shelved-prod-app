package com.authentication.demo.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Model.LikeModel;
import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.CollectionRepository;
import com.authentication.demo.Repository.LikeRepository;
import com.authentication.demo.Repository.UserRepository;

@Controller
public class LikeController {

  private final LikeRepository likeRepository;
  private final CollectionRepository collectionRepository;
  private final UserRepository userRepository;


  public LikeController(LikeRepository likeRepository, CollectionRepository collectionRepository,
      UserRepository userRepository) {
    this.likeRepository = likeRepository;
    this.collectionRepository = collectionRepository;
    this.userRepository = userRepository;

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
    return "redirect:/collections/" + collectionId;
  }

}
