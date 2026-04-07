package com.example.bloodbank.service;

import com.example.bloodbank.proxy.BloodRequestProxy;
import com.example.bloodbank.proxy.HospitalProxy;

import java.util.List;

public interface HospitalService {

   HospitalProxy getHospitalProfile(String hospitalEmail);

   String updateHospital(String hospitalEmail,HospitalProxy hospitalProxy);

   String requestBlood(String hospitalEmail ,BloodRequestProxy bloodRequestProxy);

   List<BloodRequestProxy> getHistory(String email);

}
