package com.authentication.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "likes")
public class LikeModel {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserModel user;

  @ManyToOne
  @JoinColumn(name = "collection_id", nullable = false)
  private CollectionModel collection;


  public LikeModel() {
  }

  public LikeModel(Long id, UserModel user, CollectionModel collection) {
    this.id = id;
    this.user = user;
    this.collection = collection;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserModel getUser() {
    return user;
  }

  public void setUser(UserModel user) {
    this.user = user;
  }

  public CollectionModel getCollection() {
    return collection;
  }

  public void setCollection(CollectionModel collection) {
    this.collection = collection;
  }
}
