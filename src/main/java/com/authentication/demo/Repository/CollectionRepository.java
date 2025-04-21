package com.authentication.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.authentication.demo.Model.CollectionModel;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionModel, Long> {

    List<CollectionModel> findAllByUserId(Long userId);
    List<CollectionModel> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    List<CollectionModel> findAllByOrderByCreatedAtDesc();

    @Query("SELECT c FROM CollectionModel c WHERE c.user IN " +
    "(SELECT f.followed FROM FollowModel f WHERE f.follower.id = :userId)")
    List<CollectionModel> findAllByFollowing(@Param("userId") Long userId);

    List<CollectionModel> findByTitleContainingIgnoreCase(String title);
}