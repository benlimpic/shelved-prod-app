package com.authentication.demo.Model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "items")
public class ItemModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "collection_id", nullable = false)
  private Long collectionId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "caption")
  private String caption;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Timestamp createdAt;

  @Column(name = "updated_at", nullable = false)
  private Timestamp updatedAt;

  public ItemModel() {

  }

  public ItemModel(Long id, Long userId, Long collectionId, String caption, String imageUrl, Timestamp createdAt,
      Timestamp updatedAt) {
    this.id = id;
    this.userId = userId;
    this.collectionId = collectionId;
    this.caption = caption;
    this.imageUrl = imageUrl;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Long getId() {
    return id;
  }

  public Long getCollectionId() {
    return collectionId;
  }

  public Long getUserId() {
    return userId;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
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
}
