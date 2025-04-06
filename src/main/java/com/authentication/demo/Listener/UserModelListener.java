package com.authentication.demo.Listener;

import java.sql.Timestamp;

import com.authentication.demo.Model.UserModel;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class UserModelListener {

    @PrePersist
    public void prePersist(UserModel user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate(UserModel user) {
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    }
}