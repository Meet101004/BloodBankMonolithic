package com.example.bloodbank.repository;

import com.example.bloodbank.entity.DonorDetails;
import com.example.bloodbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorDetailsRepo extends JpaRepository<DonorDetails,Long> {
    Optional<DonorDetails> findByUser(User user);
}
