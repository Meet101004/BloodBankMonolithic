package com.example.bloodbank.controller;

import com.example.bloodbank.proxy.DonateRequest;
import com.example.bloodbank.proxy.DonationProxy;
import com.example.bloodbank.proxy.DonorDetailsProxy;
import com.example.bloodbank.proxy.historyProxy;
import com.example.bloodbank.service.DonorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/donor")
public class DonorController {
    @Autowired
    private DonorService donorService;

    @GetMapping("/profile/{email}")
    public ResponseEntity<DonorDetailsProxy> getDonorProfile(@PathVariable String email){
        DonorDetailsProxy donorProfile = donorService.getDonorProfile(email);
        return ResponseEntity.ok(donorProfile);
    }

    @PostMapping("/profile/update/{donorEmail}")
    public ResponseEntity<String> updateProfile(@PathVariable String donorEmail,@Valid @RequestBody DonorDetailsProxy donorDetailsProxy){
        System.out.println(donorDetailsProxy);
        String s = donorService.updateProfile(donorEmail, donorDetailsProxy);
        return ResponseEntity.ok(s);
    }

    @PostMapping("/donate/{donorEmail}")
    public ResponseEntity<String> donateBlood(@PathVariable String donorEmail,@RequestBody Integer units){
        String s = donorService.donateBlood(donorEmail, units);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/history/{donorEmail}")
    public ResponseEntity<List<historyProxy>> getDonationHistory(@PathVariable String donorEmail){
        List<historyProxy> donationHistory = donorService.getDonationHistory(donorEmail);
        return ResponseEntity.ok(donationHistory);
    }

    @PostMapping("/donate/request/{donorEmail}")
    public ResponseEntity<String> donateRequest(@PathVariable String donorEmail,@RequestBody DonationProxy donationProxy){
        System.out.println(donorEmail);
        System.out.println("this is donorproxy:  "+donationProxy);
        String s = donorService.donateRequest(donorEmail, donationProxy);
        return ResponseEntity.ok(s);
    }


}
