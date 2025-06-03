package com.authentication.demo.TestUtils;

import java.sql.Timestamp;
import java.util.List;

import com.authentication.demo.Model.UserModel;

public class TestUserFactory {
    public static UserModel createMockUser() {
        UserModel user = new UserModel();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password");
        user.setRoles(List.of("ROLE_USER"));
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setWebsite("http://example.com");
        user.setLocation("Test Location");
        user.setBiography("This is a test user.");
        user.setProfilePictureUrl("http://example.com/profile.jpg");
        user.setCreatedAt(Timestamp.valueOf("2023-01-01 00:00:00"));
        user.setUpdatedAt(Timestamp.valueOf("2023-01-01 00:00:00"));
        return user;
    }
}