package com.authentication.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authentication.demo.Model.MyAppUser;

public interface MyAppUserRepository extends JpaRepository<MyAppUser, Long> {
  Optional<MyAppUser> findByUsername(String username);
}
