package com.example.bloodbank.service;

import com.example.bloodbank.entity.BloodRequest;
import com.example.bloodbank.entity.BloodStock;
import com.example.bloodbank.proxy.BloodRequestProxy;
import com.example.bloodbank.proxy.BloodStockProxy;
import com.example.bloodbank.proxy.DonationProxy;
import com.example.bloodbank.proxy.UserProxy;

import java.util.List;

public interface AdminService {

    List<UserProxy> getAllUsers();

    List<DonationProxy> getAllDonations();

    List<BloodRequestProxy> getAllBloodRequests();

    String approveUser(Long donorId);


    String rejectDonor(Long donorId);

    List<BloodStockProxy> seeStock();

//    String approveDonationReq(Long donationReqId);

    String  addBloodStock(Long donationReqId);

    String approveRequest(Long requestId);

    byte[] getReports();

}
