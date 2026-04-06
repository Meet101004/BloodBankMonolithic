package com.example.bloodbank.service.implementation;

import com.example.bloodbank.entity.*;
import com.example.bloodbank.exception.UserNotFoundException;
import com.example.bloodbank.proxy.BloodRequestProxy;
import com.example.bloodbank.proxy.HospitalProxy;
import com.example.bloodbank.repository.BloodReqRepo;
import com.example.bloodbank.repository.BloodStockRepo;
import com.example.bloodbank.repository.HospitalRepo;
import com.example.bloodbank.repository.UserRepo;
import com.example.bloodbank.service.HospitalService;
import com.example.bloodbank.utils.MapperHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepo hospitalRepo;
    @Autowired
    private MapperHelper mapper;
    @Autowired
    private BloodReqRepo bloodReqRepo;
    @Autowired
    private BloodStockRepo bloodStockRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public HospitalProxy getHospitalProfile(String hospitalEmail) {
        Optional<User> byEmail = userRepo.findByEmail(hospitalEmail);
        Optional<Hospital> byId = hospitalRepo.findById(byEmail.get().getId());
        if(byId.isEmpty()){
            throw new UserNotFoundException("Hospital Not Found with thid id: "+byEmail.get().getId(), HttpStatus.NOT_FOUND.value());
        }
        if(byId.isPresent()){
            Hospital hospital = byId.get();
            return mapper.entityToProxyHospital(hospital);
        }
        return null;
    }

    @Override
    public String updateHospital(Long hospitalId, HospitalProxy hospitalProxy) {
        Optional<Hospital> byId = hospitalRepo.findById(hospitalId);
        if(byId.isEmpty()){
            throw new UserNotFoundException("Hospital Not Found with thid id: "+hospitalId, HttpStatus.NOT_FOUND.value());
        }
        if(byId.isPresent()){
            Hospital hospital = byId.get();
            hospital.setHospitalName(hospitalProxy.getHospitalName());
            hospital.setAddress(hospitalProxy.getAddress());
            hospital.setContactNum(hospitalProxy.getContactNum());
            hospital.setLicenceNumber(hospitalProxy.getLicenceNumber());

            hospitalRepo.save(hospital);
        }
        return "Hospital Updated successfully";
    }

    @Override
    public String requestBlood(Long hospitalId ,BloodRequestProxy bloodRequestProxy) {
        Optional<Hospital> byId = hospitalRepo.findById(hospitalId);
        if(byId.isEmpty()){
            throw new UserNotFoundException("Hospital Not Found with thid id: "+hospitalId, HttpStatus.NOT_FOUND.value());
        }

        int remainingBlood=0;
        int totalpending=0;
        List<BloodRequest> allByBloodGroup = bloodReqRepo.findAllByBloodGroup(bloodRequestProxy.getBloodGroup());
        Optional<BloodStock> byBloodGroup = bloodStockRepo.findByBloodGroup(bloodRequestProxy.getBloodGroup());
        if(byBloodGroup.isEmpty()){
            throw new RuntimeException("BloodGroup Not Found");
        }
        for(BloodRequest b:allByBloodGroup){
            if(b.getStatus() == false){
                totalpending += b.getQuantity();
            }
        }
        remainingBlood=byBloodGroup.get().getAvailableUnits() - totalpending;
        if(remainingBlood < bloodRequestProxy.getQuantity()){
            throw new RuntimeException("Previous Request For Same BloodGroup is Pending And when ur Turn comes Blood Stock Will be Empty");
        }

        if(byId.isPresent()){
            Hospital hospital = byId.get();
            BloodRequest bloodRequest=new BloodRequest();
            bloodRequest.setHospital(hospital);
            bloodRequest.setBloodGroup(bloodRequestProxy.getBloodGroup());
            bloodRequest.setRequestDate(LocalDateTime.now());
            bloodRequest.setQuantity(bloodRequestProxy.getQuantity());
            bloodRequest.setStatus(false);

            bloodReqRepo.save(bloodRequest);
        }
        return " Blood Request Sent Successfully";
    }
}
