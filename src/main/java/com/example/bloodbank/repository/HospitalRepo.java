package com.example.bloodbank.repository;

import com.example.bloodbank.entity.Hospital;
import com.example.bloodbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalRepo extends JpaRepository<Hospital,Long> {
    Optional<Hospital> findByUser(User user);
}
