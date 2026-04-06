package com.example.bloodbank.controller;

import com.example.bloodbank.proxy.BloodRequestProxy;
import com.example.bloodbank.proxy.HospitalProxy;
import com.example.bloodbank.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @PostMapping("/profile/update/{hospitalId}")
    public ResponseEntity<String> updateHospital(@PathVariable Long hospitalId,@Valid @RequestBody HospitalProxy hospitalProxy){
        String s = hospitalService.updateHospital(hospitalId, hospitalProxy);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/profile/{hospitalEmail}")
    public ResponseEntity<HospitalProxy> getHospitalProfile(@PathVariable String hospitalEmail){
        HospitalProxy hospitalProfile = hospitalService.getHospitalProfile(hospitalEmail);
        return ResponseEntity.ok(hospitalProfile);
    }

    @PostMapping("/request/{hospitalId}")
    public ResponseEntity<String> requestBlood(@PathVariable Long hospitalId ,@Valid @RequestBody BloodRequestProxy bloodRequestProxy){
        String s = hospitalService.requestBlood(hospitalId, bloodRequestProxy);
        return ResponseEntity.ok(s);
    }

}
