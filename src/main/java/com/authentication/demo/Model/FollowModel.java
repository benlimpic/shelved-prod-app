package com.authentication.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "follows")
public class FollowModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private UserModel follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private UserModel followed;

    public FollowModel() {
    }

    public FollowModel(UserModel follower, UserModel followed) {
        this.follower = follower;
        this.followed = followed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getFollower() {
        return follower;
    }

    public void setFollower(UserModel follower) {
        this.follower = follower;
    }

    public UserModel getFollowed() {
        return followed;
    }

    public void setFollowed(UserModel followed) {
        this.followed = followed;
    }
}