package com.authentication.demo.Model;

import java.sql.Timestamp;
import java.util.List;

import com.authentication.demo.Interface.PopularEntry;

import jakarta.persistence.CascadeType;
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
@Table(name = "items")
public class ItemModel implements PopularEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "collection_id", nullable = false)
  private CollectionModel collection;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "item_link")
  private String itemLink;

  @Column(name = "caption")
  private String caption;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Timestamp createdAt;

  @Column(name = "updated_at", nullable = false)
  private Timestamp updatedAt;

  @OneToMany(mappedBy = "item", cascade = { CascadeType.ALL }, orphanRemoval = true)
  private List<CommentModel> comments;

  @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<LikeModel> likes;

  @Transient
  private Integer likeCount;

  @Transient
  private Boolean isLiked;

  @Transient
  private Integer commentCount;

  @Transient
  private Boolean isOwner;

  @Override
  public boolean isCollection() {
    return false;
  }

  public ItemModel() {

  }

  public ItemModel(Long id, Long userId, CollectionModel collection, String title, String description, String itemLink,
      String caption, String imageUrl, Timestamp createdAt,
      Timestamp updatedAt) {
    this.id = id;
    this.userId = userId;
    this.collection = collection;
    this.title = title;
    this.description = description;
    this.itemLink = itemLink;
    this.caption = caption;
    this.imageUrl = imageUrl;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CollectionModel getCollection() {
    return collection;
  }

  public void setCollection(CollectionModel collection) {
    this.collection = collection;
  }

  public Long getUserId() {
    return userId;
  }

  @Override
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getItemLink() {
    return itemLink;
  }

  public void setItemLink(String itemLink) {
    this.itemLink = itemLink;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  @Override
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

  @Override
  public int getLikeCount() {
    return likeCount != null ? likeCount : 0;
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

  @Override
  public int getCommentCount() {
    return commentCount != null ? commentCount : 0;
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

  @Override
  public int getPopularityScore() {
    // Example implementation: you can adjust the logic as needed
    return getLikeCount() + getCommentCount();
  }
}
