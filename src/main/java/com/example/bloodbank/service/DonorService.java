package com.example.bloodbank.service;

import com.example.bloodbank.proxy.DonateRequest;
import com.example.bloodbank.proxy.DonationProxy;
import com.example.bloodbank.proxy.DonorDetailsProxy;
import com.example.bloodbank.proxy.historyProxy;

import java.util.List;

public interface DonorService {

    DonorDetailsProxy getDonorProfile(String email);

    String updateProfile(String donorEmail,DonorDetailsProxy donorDetailsProxy);

    String  donateBlood(String donorEmail,Integer units);

    String donateRequest(String donorEmail,DonationProxy donationProxy);

    List<historyProxy> getDonationHistory(String donorEmail);


}
