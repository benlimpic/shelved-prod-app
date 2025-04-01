package com.authentication.demo.Listener;

import java.sql.Timestamp;

import com.authentication.demo.Model.CollectionModel;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class CollectionModelListener {

    @PrePersist
    public void prePersist(CollectionModel collection) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        collection.setCreated_at(now);
        collection.setUpdated_at(now);
    }

    @PreUpdate
    public void preUpdate(CollectionModel collection) {
        collection.setUpdated_at(new Timestamp(System.currentTimeMillis()));
    }
}