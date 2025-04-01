package com.authentication.demo.Listener;

import java.sql.Timestamp;

import com.authentication.demo.Model.UserModel;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class UserModelListener {

    @PrePersist
    public void prePersist(UserModel user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        user.setCreated_at(now);
        user.setUpdated_at(now);
    }

    @PreUpdate
    public void preUpdate(UserModel user) {
        user.setUpdated_at(new Timestamp(System.currentTimeMillis()));
    }
}