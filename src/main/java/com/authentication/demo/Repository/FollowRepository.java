package com.authentication.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.authentication.demo.Model.FollowModel;
import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Model.CollectionModel; // Ensure this is the correct package for CollectionModel

@Repository
public interface FollowRepository extends JpaRepository<FollowModel, Long> {
    // Find all followers of a specific user
    List<FollowModel> findByFollowed(UserModel followed);

    // Find all users a specific user is following
    List<FollowModel> findByFollower(UserModel follower);

    // Check if a follow relationship exists
    boolean existsByFollowerAndFollowed(UserModel follower, UserModel followed);

    // Find a specific follow relationship
    Optional<FollowModel> findByFollowerAndFollowed(UserModel follower, UserModel followed);

    // Count followers of a user
    long countByFollowed(UserModel followed);

    // Count users a user is following
    long countByFollower(UserModel follower);

}
