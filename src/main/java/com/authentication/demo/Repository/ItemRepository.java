package com.authentication.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authentication.demo.Model.ItemModel;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel, Long> {
  List<ItemModel> findAllByCollectionIdOrderByCreatedAtDesc(Long collectionId);
  List<ItemModel> findByCollectionId(Long collectionId);
}