package com.example.bloodbank.repository;

import com.example.bloodbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String username);
}
