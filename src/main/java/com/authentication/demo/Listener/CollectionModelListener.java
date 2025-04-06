package com.authentication.demo.Listener;

import java.sql.Timestamp;

import com.authentication.demo.Model.CollectionModel;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class CollectionModelListener {

        @PrePersist
    public void prePersist(CollectionModel collection) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        collection.setCreatedAt(now);
        collection.setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate(CollectionModel collection) {
        collection.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    }
}