package com.example.bloodbank.controller;

import com.example.bloodbank.proxy.BloodRequestProxy;
import com.example.bloodbank.proxy.HospitalProxy;
import com.example.bloodbank.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @PostMapping("/profile/update/{hospitalEmail}")
    public ResponseEntity<String> updateHospital(@PathVariable String hospitalEmail,@Valid @RequestBody HospitalProxy hospitalProxy){
        String s = hospitalService.updateHospital(hospitalEmail, hospitalProxy);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/profile/{hospitalEmail}")
    public ResponseEntity<HospitalProxy> getHospitalProfile(@PathVariable String hospitalEmail){
        HospitalProxy hospitalProfile = hospitalService.getHospitalProfile(hospitalEmail);
        return ResponseEntity.ok(hospitalProfile);
    }

    @PostMapping("/request/{hospitalEmail}")
    public ResponseEntity<String> requestBlood(@PathVariable String hospitalEmail ,@Valid @RequestBody BloodRequestProxy bloodRequestProxy){
        String s = hospitalService.requestBlood(hospitalEmail, bloodRequestProxy);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/history/{email}")
    public  ResponseEntity<List<BloodRequestProxy>> getHistory(@PathVariable String email){
        List<BloodRequestProxy> history = hospitalService.getHistory(email);
        return ResponseEntity.ok(history);
    }


}
