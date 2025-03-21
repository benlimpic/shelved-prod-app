package com.authentication.demo.Model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String website;
    private String location;
    private String biography;
    private String profilePictureUrl;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username, String password, String email, String firstName, String lastName, String location, String website, String biography, String profilePictureUrl, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.website = website;
        this.location = location;
        this.biography = biography;
        this.profilePictureUrl = profilePictureUrl;
        this.authorities = authorities;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getBiography() {
        return biography;
    }


    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}