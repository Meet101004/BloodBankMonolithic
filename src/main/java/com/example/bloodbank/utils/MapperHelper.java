package com.example.bloodbank.utils;

import com.example.bloodbank.entity.*;
import com.example.bloodbank.proxy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class MapperHelper {

    @Autowired
    private ObjectMapper mapper;

    public UserProxy entityToProxyUser(User user){
        return mapper.convertValue(user, UserProxy.class);
    }

    public User proxyToEntityUser(UserProxy userProxy){
        return mapper.convertValue(userProxy, User.class);
    }

    public DonorDetailsProxy entityToProxyDonor(DonorDetails donorDetails){
        return mapper.convertValue(donorDetails, DonorDetailsProxy.class);
    }

    public DonorDetails proxyToEntityDonor(DonorDetailsProxy donorDetailsProxy){
        return mapper.convertValue(donorDetailsProxy, DonorDetails.class);
    }

    public DonationProxy entityToProxyDonation(Donation donation){
        return mapper.convertValue(donation, DonationProxy.class);
    }

    public Donation proxyToEntityDonation(DonationProxy donationProxy){
        return mapper.convertValue(donationProxy, Donation.class);
    }

    public HospitalProxy entityToProxyHospital(Hospital hospital){
        return mapper.convertValue(hospital, HospitalProxy.class);
    }

    public Hospital proxyToEntityDonationHospital(HospitalProxy hospitalProxy){
        return mapper.convertValue(hospitalProxy, Hospital.class);
    }

    public historyProxy entityToProxyHospitalHistory(Donation donation){
        return mapper.convertValue(donation, historyProxy.class);
    }

    public BloodRequestProxy entityToProxyBloodRequest(BloodRequest bloodRequest){
        return mapper.convertValue(bloodRequest, BloodRequestProxy.class);
    }

    public BloodStockProxy entityToProxyBloodStock(BloodStock bloodStock){
        return mapper.convertValue(bloodStock, BloodStockProxy.class);
    }
}
