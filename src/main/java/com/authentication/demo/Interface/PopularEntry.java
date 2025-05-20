package com.authentication.demo.Interface;

public interface PopularEntry {
    int getLikeCount();
    int getCommentCount();
    int getPopularityScore();
    String getImageUrl();
    String getTitle();
    Long getId();
    boolean isCollection(); // or getType()
}
