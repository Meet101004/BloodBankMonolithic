package com.example.bloodbank.repository;

import com.example.bloodbank.entity.Donation;
import com.example.bloodbank.entity.DonorDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DonationRepo extends JpaRepository<Donation,Long> {

    List<Donation> findAllByDonorDetails(DonorDetails donor);

}
