package com.authentication.demo.Model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "collections")
public class CollectionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(name = "user_id", nullable = false)
    private final Long userId;

    @Column(name = "caption", nullable = false)
    private String caption;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

  public CollectionModel(Long id, Long userId, String caption, String description, String imageUrl, Timestamp created_at, Timestamp updated_at) {
    this.id = id;
    this.userId = userId;
    this.caption = caption;
    this.description = description;
    this.imageUrl = imageUrl;
    this.created_at = (created_at == null) ? new Timestamp(System.currentTimeMillis()) : created_at;
    this.updated_at = new Timestamp(System.currentTimeMillis());
}

  public Long getId() {
    return id;
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

  public Timestamp getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Timestamp created_at) {
    this.created_at = created_at;
  }

  public Timestamp getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(Timestamp updated_at) {
    this.updated_at = updated_at;
  }
}
