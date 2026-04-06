package com.example.bloodbank.repository;

import com.example.bloodbank.entity.BloodRequest;
import com.example.bloodbank.entity.Donation;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface BloodReqRepo extends JpaRepository<BloodRequest,Long> {
    List<BloodRequest> findAllByBloodGroup(@NotBlank(message = "BloodGroup cannot Blank") String bloodGroup);
}
