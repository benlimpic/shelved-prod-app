package com.authentication.demo.Model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.authentication.demo.Listener.UserModelListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


@Entity
@EntityListeners(UserModelListener.class)
@Table(name = "users")
public class UserModel implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "location")
    private String location;

    @Column(name = "biography", length = 250)
    private String biography;

    @Column(name = "profile_picture", length = 1024)
    private String profilePictureUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CollectionModel> collections;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<LikeModel> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentModel> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReplyModel> replies;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FollowModel> following;

    @OneToMany(mappedBy = "followed", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FollowModel> followers;


    @Transient
    private boolean isFollowing;



    public UserModel() {
    }

    public UserModel(
        Long id,
        String username,
        String password,
        String firstName,
        String lastName,
        String email,
        String website,
        String location,
        String biography,
        String profilePictureUrl,
        List<String> roles,
        java.sql.Timestamp createdAt,
        java.sql.Timestamp updatedAt) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.website = website;
        this.location = location;
        this.biography = biography;
        this.profilePictureUrl = profilePictureUrl;
        this.roles = roles;
        this.createdAt = (createdAt == null) ? new Timestamp(System.currentTimeMillis()) : createdAt;
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = (createdAt == null) ? new Timestamp(System.currentTimeMillis()) : createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

        public List<CollectionModel> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionModel> collections) {
        this.collections = collections;
    }

    public List<LikeModel> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeModel> likes) {
        this.likes = likes;
    }

    public List<CommentModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }

    public List<ReplyModel> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyModel> replies) {
        this.replies = replies;
    }

    public List<FollowModel> getFollowing() {
        return following;
    }

    public void setFollowing(List<FollowModel> following) {
        this.following = following;
    }

    public List<FollowModel> getFollowers() {
        return followers;
    }

    public void setFollowers(List<FollowModel> followers) {
        this.followers = followers;
    }

    public void addCollection(CollectionModel collection) {
        this.collections.add(collection);
        collection.setUser(this);
    }

    public void removeCollection(CollectionModel collection) {
        this.collections.remove(collection);
        collection.setUser(null);
    }

    public void addLike(LikeModel like) {
        this.likes.add(like);
        like.setUser(this);
    }

    public void removeLike(LikeModel like) {
        this.likes.remove(like);
        like.setUser(null);
    }

    public void addComment(CommentModel comment) {
        this.comments.add(comment);
        comment.setUser(this);
    }

    public void removeComment(CommentModel comment) {
        this.comments.remove(comment);
        comment.setUser(null);
    }

    public void addReply(ReplyModel reply) {
        this.replies.add(reply);
        reply.setUser(this);
    }

    public void removeReply(ReplyModel reply) {
        this.replies.remove(reply);
        reply.setUser(null);
    }

    public void addFollowing(FollowModel follow) {
        this.following.add(follow);
        follow.setFollower(this);
    }

    public void removeFollowing(FollowModel follow) {
        this.following.remove(follow);
        follow.setFollower(null);
    }

    public void addFollower(FollowModel follow) {
        this.followers.add(follow);
        follow.setFollowed(this);
    }

    public void removeFollower(FollowModel follow) {
        this.followers.remove(follow);
        follow.setFollowed(null);
    }

    


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}



