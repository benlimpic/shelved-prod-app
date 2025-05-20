package com.authentication.demo.Interface;

public interface PopularEntry {
    int getLikeCount();
    int getCommentCount();
    String getImageUrl();
    String getTitle();
    Long getId();
    boolean isCollection(); // or getType()
}
