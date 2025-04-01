package com.authentication.demo.Model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.authentication.demo.Listener.UserModelListener;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@EntityListeners(UserModelListener.class)
@Table(name = "users")
public class UserModel implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "location")
    private String location;

    @Column(name = "biography")
    private String biography;

    @Column(name = "profile_picture")
    private String profilePictureUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp created_at;
    
    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

    public UserModel() {
    }

    public UserModel(
        Long id,
        String username,
        String password,
        String firstName,
        String lastName,
        String email,
        String website,
        String location,
        String biography,
        String profilePictureUrl,
        List<String> roles,
        java.sql.Timestamp created_at,
        java.sql.Timestamp updated_at) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.website = website;
        this.location = location;
        this.biography = biography;
        this.profilePictureUrl = profilePictureUrl;
        this.roles = roles;
        this.created_at = (created_at == null) ? new Timestamp(System.currentTimeMillis()) : created_at;
        this.updated_at = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = (created_at == null) ? new Timestamp(System.currentTimeMillis()) : created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }
    
    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = new Timestamp(System.currentTimeMillis());
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
