package com.authentication.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authentication.demo.Model.LikeModel;

public interface LikeRepository extends JpaRepository<LikeModel, Long> {
  List<LikeModel> findAllByCollectionId(Long collectionId);

  List<LikeModel> findAllByItemId(Long itemId);

  List<LikeModel> findAllByUserId(Long userId);

  int countByCollectionId(Long collectionId);

  int countByItemId(Long itemId);

}
