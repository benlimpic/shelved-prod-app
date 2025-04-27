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
  @JoinColumn(name = "collection_id")
  private CollectionModel collection;

  @ManyToOne
  @JoinColumn(name = "item_id")
  private ItemModel item;

  @ManyToOne
  @JoinColumn(name = "comment_id")
  private CommentModel comment;

  public LikeModel() {
  }

  public LikeModel(Long id, UserModel user, CollectionModel collection, ItemModel item, CommentModel comment) {
    this.id = id;
    this.user = user;
    this.collection = collection;
    this.item = item;
    this.comment = comment;
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

  public ItemModel getItem() {
    return item;
  }

  public void setItem(ItemModel item) {
    this.item = item;
  }

  public CommentModel getComment() {
    return comment;
  }

  public void setComment(CommentModel comment) {
    this.comment = comment;
  }
}
