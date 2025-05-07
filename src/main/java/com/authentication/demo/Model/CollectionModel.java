package com.authentication.demo.Model;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "collections")
public class CollectionModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserModel user;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "caption")
  private String caption;

  @Column(name = "description")
  private String description;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Timestamp createdAt;

  @Column(name = "updated_at", nullable = false)
  private Timestamp updatedAt;

  @OneToMany(mappedBy = "collection", cascade = { jakarta.persistence.CascadeType.ALL }, orphanRemoval = true)
  private List<ItemModel> items;

  @OneToMany(mappedBy = "collection", cascade = { jakarta.persistence.CascadeType.ALL }, orphanRemoval = true)
  private List<CommentModel> comments;

  @Transient
  private Integer likeCount;

  @Transient
  private Boolean isLiked;

  @Transient
  private Integer commentCount;

  @Transient
  private Boolean isOwner;

  public CollectionModel() {

  }

  public CollectionModel(
      Long id,
      UserModel user,
      String title,
      String caption,
      String description,
      String imageUrl,
      Timestamp createdAt,
      Timestamp updatedAt,
      List<ItemModel> items) {

    this.id = id;
    this.user = user;
    this.title = title;
    this.caption = caption;
    this.description = description;
    this.imageUrl = imageUrl;
    this.createdAt = (createdAt == null) ? new Timestamp(System.currentTimeMillis()) : createdAt;
    this.updatedAt = new Timestamp(System.currentTimeMillis());
    this.items = items;
  }

  public Long getId() {
    return id;
  }

  public UserModel getUser() {
    return user;
  }

  public void setUser(UserModel user) {
    this.user = user;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
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

  public List<ItemModel> getItems() {
    return items;
  }

  public void setItems(List<ItemModel> items) {
    this.items = items;
  }

  public Integer getLikeCount() {
    return likeCount;
  }

  public void setLikeCount(Integer likeCount) {
    this.likeCount = likeCount;
  }

  public Boolean getIsLiked() {
    return isLiked;
  }

  public void setIsLiked(Boolean isLiked) {
    this.isLiked = isLiked;
  }

  public List<CommentModel> getComments() {
    return comments;
  }

  public void setComments(List<CommentModel> comments) {
    this.comments = comments;
  }

  public void addComment(CommentModel comment) {
    this.comments.add(comment);
    comment.setCollection(this);
  }

  public void removeComment(CommentModel comment) {
    this.comments.remove(comment);
    comment.setCollection(null);
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public Boolean getIsOwner() {
    return isOwner;
  }

  public void setIsOwner(Boolean isOwner) {
    this.isOwner = isOwner;
  }
}
