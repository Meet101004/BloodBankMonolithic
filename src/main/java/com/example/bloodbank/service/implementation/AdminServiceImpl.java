package com.example.bloodbank.service.implementation;

import com.example.bloodbank.entity.*;
import com.example.bloodbank.exception.UserNotFoundException;
import com.example.bloodbank.proxy.BloodRequestProxy;
import com.example.bloodbank.proxy.BloodStockProxy;
import com.example.bloodbank.proxy.DonationProxy;
import com.example.bloodbank.proxy.UserProxy;
import com.example.bloodbank.repository.*;
import com.example.bloodbank.service.AdminService;
import com.example.bloodbank.utils.MapperHelper;
import com.example.bloodbank.utils.StorageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DonorDetailsRepo donorDetailsRepo;

    @Autowired
    private MapperHelper mapper;

    @Autowired
    private BloodReqRepo bloodReqRepo;

    @Autowired
    private BloodStockRepo bloodStockRepo;

    @Autowired
    private DonationRepo donationRepo;

    @Autowired
    private HospitalRepo hospitalRepo;

    @Override
    public List<UserProxy> getAllUsers() {
        List<User> all = userRepo.findAll();
        return all.stream().map(u->mapper.entityToProxyUser(u)).toList();
    }

    @Override
    public List<DonationProxy> getAllDonations() {
        List<Donation> all = donationRepo.findAll();
        return all.stream().map(d->mapper.entityToProxyDonation(d)).toList();
    }

    @Override
    public List<BloodRequestProxy> getAllBloodRequests() {
        List<BloodRequest> all = bloodReqRepo.findAll();
        return all.stream().map(b->mapper.entityToProxyBloodRequest(b)).toList();
    }

    @Override
    public String approveUser(Long userId) {
        Optional<User> byId = userRepo.findById(userId);
        if(byId.isPresent()){
            User user = byId.get();
            if(user.getStatus() == true){
                throw new UserNotFoundException("User Already Approved with id: "+user.getId(),HttpStatus.OK.value());
            }
            if(user.getRole().equals("DONOR")){
                user.setStatus(true);
                DonorDetails donorDetails=new DonorDetails();
                donorDetails.setUser(user);
                donorDetailsRepo.save(donorDetails);
                return "Donor Approved Successfully";
            }
            if(user.getRole().equals("HOSPITAL")){
                user.setStatus(true);
                Hospital hospital=new Hospital();
                hospital.setUser(user);
                hospitalRepo.save(hospital);
                return "Hospital Approved Successfully";
            }
            return "User Not Approved";
        }
        return null;
    }

    @Override
    public String rejectDonor(Long userId) {
        Optional<User> byId = userRepo.findById(userId);
        if(byId.isPresent()){
            User user = byId.get();
            user.setStatus(false);
            return "Donor Rejected Successfully";
        }
        return null;
    }

    @Override
    public List<BloodStockProxy> seeStock() {
        List<BloodStock> all = bloodStockRepo.findAll();
        return all.stream().map(s->mapper.entityToProxyBloodStock(s)).toList();
    }

    @Override
    public String addBloodStock(Long donationReqId) {
        Optional<Donation> byId = donationRepo.findById(donationReqId);
        if(byId.isEmpty()){
            throw new UserNotFoundException("Donate Req Not Found: "+donationReqId,HttpStatus.NOT_FOUND.value());
        }
        if(byId.isPresent()){
            Donation donation = byId.get();
            if(donation.getStatus() != false) {
                throw new RuntimeException("Request Already Processed");
            }
                Optional<BloodStock> byBloodGroupOptional = bloodStockRepo.findByBloodGroup(donation.getBloodGroup());
                if (byBloodGroupOptional.isPresent()) {
                    BloodStock bloodStock = byBloodGroupOptional.get();
                    bloodStock.setAvailableUnits(bloodStock.getAvailableUnits() + donation.getQuantity());
                    bloodStock.setLastUpdated(LocalDateTime.now());

                    donation.setStatus(true);
                    donationRepo.save(donation);
                    bloodStockRepo.save(bloodStock);
                } else {
                    BloodStock stock = new BloodStock();
                    stock.setBloodGroup(donation.getBloodGroup());
                    stock.setAvailableUnits(donation.getQuantity());
                    stock.setLastUpdated(LocalDateTime.now());

                    donation.setStatus(true);
                    donationRepo.save(donation);
                    bloodStockRepo.save(stock);
                }
        }
        return "Stock Added Successfully";
    }

    @Override
    public String approveRequest(Long requestId) {
        Optional<BloodRequest> byId = bloodReqRepo.findById(requestId);
        if(byId.isEmpty()){
           throw new UserNotFoundException("Request Not Present with this id: "+requestId, HttpStatus.NOT_FOUND.value());
        }
        BloodRequest bloodRequest = byId.get();
        if(bloodRequest.getStatus() != false){
            throw new RuntimeException("Request Already Processed");
        }
        if(byId.isPresent()){
            Optional<BloodStock> byBloodGroup = bloodStockRepo.findByBloodGroup(bloodRequest.getBloodGroup());
            if(byBloodGroup.isEmpty()){
                bloodRequest.setStatus(true);
                bloodReqRepo.save(bloodRequest);
                throw new UserNotFoundException("Sorry BloodGroup Not Available: "+bloodRequest.getBloodGroup(), HttpStatus.NOT_FOUND.value());
            }
            if(byBloodGroup.isPresent()){
                BloodStock bloodStock = byBloodGroup.get();
                if(bloodStock.getAvailableUnits() >= bloodRequest.getQuantity()){
                    bloodStock.setAvailableUnits((bloodStock.getAvailableUnits()) - (bloodRequest.getQuantity()));
                }else{
                    bloodRequest.setStatus(true);
                    bloodReqRepo.save(bloodRequest);
                    throw new UserNotFoundException("Too Much Quantity You Are Asking for "+bloodRequest.getBloodGroup()+" : "+bloodRequest.getQuantity()+" But We have Only "+bloodStock.getAvailableUnits()+" Units Available",HttpStatus.NOT_FOUND.value());
                }
                bloodStockRepo.save(bloodStock);

                bloodRequest.setStatus(true);
                bloodReqRepo.save(bloodRequest);
            }
        }
        return "Request Approved Successfully";
    }

    @Override
    public byte[] getReports() {
        List<Donation> donationList = donationRepo.findAll();
        List<BloodRequest> bloodRequestList = bloodReqRepo.findAll();
        List<BloodStock> bloodStockList = bloodStockRepo.findAll();
        return StorageHelper.getExcelFile(donationList,bloodRequestList,bloodStockList);
    }
}
