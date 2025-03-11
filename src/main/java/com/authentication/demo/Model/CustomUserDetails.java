package com.authentication.demo.Model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String aboutMe; // Use aboutMe property
    private final byte[] profilePicture; // Use profilePicture property
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username, String password, String email, String firstName, String lastName, String aboutMe, byte[] profilePicture, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.aboutMe = aboutMe;
        this.profilePicture = profilePicture;
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

    public String getAboutMe() {
        return aboutMe;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
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