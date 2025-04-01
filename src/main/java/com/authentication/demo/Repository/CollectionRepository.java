package com.authentication.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authentication.demo.Model.CollectionModel;

public interface CollectionRepository extends JpaRepository<CollectionModel, Long> {
    public CollectionModel findByUserId(Long userId);
    List<CollectionModel> findAllByUserId(Long userId);
}