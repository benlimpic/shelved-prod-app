package com.authentication.demo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.authentication.demo.Model.LikeModel;
import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.CollectionRepository;
import com.authentication.demo.Repository.LikeRepository;
import com.authentication.demo.Repository.UserRepository;

@Service
public class LikeService {

  private final LikeRepository likeRepository;
  private final CollectionRepository collectionRepository;
  private final UserRepository userRepository;


  public LikeService(LikeRepository likeRepository, CollectionRepository collectionRepository,
      UserRepository userRepository) {
    this.likeRepository = likeRepository;
    this.collectionRepository = collectionRepository;
    this.userRepository = userRepository;
  }

  public void toggleLike(Long userId, Long collectionId) {
    UserModel currentUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
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
      newLike.setCollection(collectionRepository.findById(collectionId)
          .orElseThrow(() -> new RuntimeException("Collection not found")));
      likeRepository.save(newLike);
    }

  }

  public Integer countLikes(Long collectionId) {
    return likeRepository.countByCollectionId(collectionId);
  }

}
