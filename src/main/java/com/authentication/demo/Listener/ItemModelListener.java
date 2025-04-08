package com.authentication.demo.Listener;

import java.sql.Timestamp;

import com.authentication.demo.Model.ItemModel;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class ItemModelListener {

  @PrePersist
  public void prePersist(ItemModel item) {
      Timestamp now = new Timestamp(System.currentTimeMillis());
      item.setCreatedAt(now);
      item.setUpdatedAt(now);
  }

  @PreUpdate
  public void preUpdate(ItemModel item) {
      item.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
  }
}
