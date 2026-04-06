package com.example.bloodbank.service;

import com.example.bloodbank.proxy.BloodRequestProxy;
import com.example.bloodbank.proxy.HospitalProxy;

public interface HospitalService {

   HospitalProxy getHospitalProfile(String hospitalEmail);

   String updateHospital(Long hospitalId,HospitalProxy hospitalProxy);

   String requestBlood(Long hospitalId,BloodRequestProxy bloodRequestProxy);

}
