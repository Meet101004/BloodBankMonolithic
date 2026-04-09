package com.example.bloodbank.service;

import com.example.bloodbank.proxy.BloodRequestProxy;
import com.example.bloodbank.proxy.BloodStockProxy;
import com.example.bloodbank.proxy.DonationProxy;
import com.example.bloodbank.proxy.UserProxy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {

    Page<UserProxy> getAllUsers(Pageable pageable);

    Page<DonationProxy> getAllDonations(Pageable pageable);

    Page<BloodRequestProxy> getAllBloodRequests(Pageable pageable);

    String approveUser(Long donorId);

    String rejectDonor(Long donorId);

    List<BloodStockProxy> seeStock();

    String addBloodStock(Long donationReqId);

    String approveRequest(Long requestId);

    byte[] getReports();

}
