package com.example.bloodbank.service.implementation;

import com.example.bloodbank.entity.BloodStock;
import com.example.bloodbank.entity.Donation;
import com.example.bloodbank.entity.DonorDetails;
import com.example.bloodbank.entity.User;
import com.example.bloodbank.exception.UserNotFoundException;
import com.example.bloodbank.proxy.DonateRequest;
import com.example.bloodbank.proxy.DonationProxy;
import com.example.bloodbank.proxy.DonorDetailsProxy;
import com.example.bloodbank.proxy.historyProxy;
import com.example.bloodbank.repository.BloodStockRepo;
import com.example.bloodbank.repository.DonationRepo;
import com.example.bloodbank.repository.DonorDetailsRepo;
import com.example.bloodbank.repository.UserRepo;
import com.example.bloodbank.service.DonorService;
import com.example.bloodbank.utils.MapperHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DonorServiceImpl implements DonorService {

    @Autowired
    private DonorDetailsRepo donorDetailsRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MapperHelper mapper;

    @Autowired
    private DonationRepo donationRepo;

    @Autowired
    private BloodStockRepo bloodStockRepo;

    @Override
    public DonorDetailsProxy getDonorProfile(String email) {
        Optional<User> byEmail = userRepo.findByEmail(email);
        if(byEmail.isEmpty()){
            throw new  UserNotFoundException("User Not Found with this email: "+email, HttpStatus.NOT_FOUND.value());
        }
        Optional<DonorDetails> byUser = donorDetailsRepo.findByUser(byEmail.get());
        DonorDetailsProxy donorDetailsProxy = mapper.entityToProxyDonor(byUser.get());
        return donorDetailsProxy;
    }

    @Override
    public String updateProfile(String donorEmail, DonorDetailsProxy donorDetailsProxy) {
//        Optional<User> byId = userRepo.findById(donorId);
//        if(byId.isEmpty()){
//            return "User Not Found";
//        }
//        User user = byId.get();
//        if(!user.getRole().equals("DONOR")){
//            return "User is Not Donor";
//        }
        Optional<User> byuser = userRepo.findByEmail(donorEmail);
        if(byuser.isEmpty()){
            throw new UserNotFoundException("User Not Found",HttpStatus.NOT_FOUND.value());
        }
        User user = byuser.get();

        Optional<DonorDetails> byId = donorDetailsRepo.findByUser(user);
        System.out.println(byId);
        if(byId.isEmpty()){
            throw new UserNotFoundException("User Not Found with id: "+byId,HttpStatus.NOT_FOUND.value());
        }
        if(byId.isPresent()){
            DonorDetails donorDetails = byId.get();
            donorDetails.setAge(donorDetailsProxy.getAge());
            donorDetails.setBloodGroup(donorDetailsProxy.getBloodGroup());
            donorDetails.setCity(donorDetailsProxy.getCity());
            donorDetails.setGender(donorDetailsProxy.getGender());
            donorDetails.setAvailable(donorDetailsProxy.getAvailable());
            donorDetails.setLastDonationDate(LocalDateTime.now());
           // donorDetails.getUser().setName(donorDetailsProxy.getName());
            if (donorDetailsProxy.getUser() != null && donorDetailsProxy.getUser().getName() != null) {
                user.setName(donorDetailsProxy.getUser().getName());
                userRepo.save(user);
            }
            donorDetailsRepo.save(donorDetails);
        }
//        else{
//            donorDetails=new DonorDetails();
//            donorDetails.setUser(user);
//        }

        return "Profile Updated Successfully";
    }
    @Override
    public String donateBlood(String donorEmail, Integer units) {
        Optional<User> userOptional = userRepo.findByEmail(donorEmail);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found with email: " + donorEmail, HttpStatus.NOT_FOUND.value());
        }
        User user = userOptional.get();

        Optional<DonorDetails> donorOptional = donorDetailsRepo.findByUser(user);
        if (donorOptional.isEmpty()) {
            throw new UserNotFoundException("Donor not found for user: " + donorEmail, HttpStatus.NOT_FOUND.value());
        }
        DonorDetails donor = donorOptional.get();

        Donation donation = new Donation();
        donation.setDonorDetails(donor);
        donation.setDonationDate(LocalDateTime.now());
        donation.setBloodGroup(donor.getBloodGroup());
        donation.setQuantity(units);
        donation.setStatus(true);
        donationRepo.save(donation);

        BloodStock bloodStock = bloodStockRepo.findByBloodGroup(donor.getBloodGroup())
                .orElseGet(() -> {
                    BloodStock stock = new BloodStock();
                    stock.setBloodGroup(donor.getBloodGroup());
                    stock.setAvailableUnits(0);
                    return stock;
                });

        bloodStock.setAvailableUnits(bloodStock.getAvailableUnits() + units);
        bloodStock.setLastUpdated(LocalDateTime.now());
        bloodStockRepo.save(bloodStock);

        return "Blood Donated Successfully";
    }

    @Override
    public String donateRequest(String donorEmail, DonationProxy donationProxy) {
        Optional<User> byEmail = userRepo.findByEmail(donorEmail);
        if(byEmail.isEmpty()){
            throw new RuntimeException("User Not Found");
        }
        Optional<DonorDetails> byId = donorDetailsRepo.findById(byEmail.get().getId());
        if(byId.isEmpty()){
            throw new UserNotFoundException("Donor Not Found: "+byEmail.get().getId(),HttpStatus.NOT_FOUND.value());
        }

        if(byId.isPresent()){
            DonorDetails donorDetails = byId.get();
            Donation donation=new Donation();
            donation.setDonorDetails(donorDetails);
            donation.setDonorName(donorDetails.getUser().getName());
            donation.setBloodGroup(donorDetails.getBloodGroup());
            donation.setDonationDate(LocalDateTime.now());
            donation.setQuantity(donationProxy.getQuantity());

            donationRepo.save(donation);
        }
        return "Donate Request Sent Successfully";
    }

    @Override
    public List<historyProxy> getDonationHistory(String donorEmail) {
        Optional<User> byEmail = userRepo.findByEmail(donorEmail);
        User user = byEmail.get();
        if(byEmail.isEmpty()){
            throw new RuntimeException("Donor Not FOund");
        }
        Optional<DonorDetails> byId = donorDetailsRepo.findByUser(user);
        if(byId.isEmpty()){
            throw new UserNotFoundException("Donor Not Found with id: "+byEmail.get().getId(),HttpStatus.NOT_FOUND.value());
        }
        if(byId.isPresent()){
            DonorDetails donor = byId.get();
            List<Donation> allByDonorDetails = donationRepo.findAllByDonorDetails(donor);
            if(allByDonorDetails.isEmpty()){
                throw new UserNotFoundException("Donor Not Found with id: "+byEmail.get().getId(),HttpStatus.NOT_FOUND.value());
            }
            return allByDonorDetails.stream().map(d->mapper.entityToProxyHospitalHistory(d)).toList();
        }
        return null;
    }
}
