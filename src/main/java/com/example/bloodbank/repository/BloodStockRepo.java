package com.example.bloodbank.repository;

import com.example.bloodbank.entity.BloodStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BloodStockRepo extends JpaRepository<BloodStock,Long> {
    Optional<BloodStock> findByBloodGroup(String bloodGroup);
}
