package com.authentication.demo.Service;

import org.springframework.stereotype.Service;

import com.authentication.demo.Model.FollowModel;
import com.authentication.demo.Repository.FollowRepository;
import com.authentication.demo.Model.UserModel;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public void followUser(UserModel follower, UserModel followed) {
        FollowModel follow = new FollowModel(follower, followed);
        followRepository.save(follow);
    }

    public boolean isFollowing(UserModel follower, UserModel followed) {
        return followRepository.existsByFollowerAndFollowed(follower, followed);
    }

    public void unfollowUser(UserModel follower, UserModel followed) {
        FollowModel follow = followRepository.findByFollowerAndFollowed(follower, followed)
            .orElseThrow(() -> new IllegalArgumentException("Follow relationship not found"));
        followRepository.delete(follow);
    }
}


