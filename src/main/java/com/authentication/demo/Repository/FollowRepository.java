package com.authentication.demo.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.authentication.demo.Model.FollowModel;
import com.authentication.demo.Model.UserModel;

@Repository
public interface FollowRepository extends JpaRepository<FollowModel, Long> {
    boolean existsByFollowerAndFollowed(UserModel follower, UserModel followed);
    Optional<FollowModel> findByFollowerAndFollowed(UserModel follower, UserModel followed);
}
