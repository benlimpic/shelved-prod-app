package com.authentication.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authentication.demo.Model.CollectionModel;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionModel, Long> {

    List<CollectionModel> findAllByUserId(Long userId);
    List<CollectionModel> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    List<CollectionModel> findAllByOrderByCreatedAtDesc();
}