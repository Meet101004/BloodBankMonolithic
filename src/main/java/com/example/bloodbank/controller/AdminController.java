package com.example.bloodbank.controller;

import com.example.bloodbank.proxy.BloodRequestProxy;
import com.example.bloodbank.proxy.BloodStockProxy;
import com.example.bloodbank.proxy.DonationProxy;
import com.example.bloodbank.proxy.UserProxy;
import com.example.bloodbank.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<Page<UserProxy>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    @GetMapping("/donations")
    public ResponseEntity<Page<DonationProxy>> getAllDonations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(adminService.getAllDonations(pageable));
    }

    @GetMapping("/bloodrequests")
    public ResponseEntity<Page<BloodRequestProxy>> getAllBloodRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(adminService.getAllBloodRequests(pageable));
    }


    @GetMapping("/donor/{userId}/approve")
    public ResponseEntity<String> approveDonor(@PathVariable String userId){
        System.out.println(userId);
        String s = adminService.approveUser(Long.parseLong(userId));
        return ResponseEntity.ok(s);
    }
    @GetMapping("/donor/{userId}/reject")
    public ResponseEntity<String> rejectDonor(@PathVariable Long userId){
        String s = adminService.rejectDonor(userId);
        return ResponseEntity.ok(s);
    }


    @GetMapping("/hospital/{requestId}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable Long requestId){
        String s = adminService.approveRequest(requestId);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/stock")
    public ResponseEntity<List<BloodStockProxy>> seeStock(){
        List<BloodStockProxy> bloodStockProxies = adminService.seeStock();
        return ResponseEntity.ok(bloodStockProxies);
    }

    @GetMapping("/reports")
    public ResponseEntity<byte[]> getReports(){
        byte[] bytes = adminService.getReports();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\n"))
                .body(bytes);
    }

    @GetMapping("/blood-stock/add/{donationReqId}")
    public ResponseEntity<String> addBloodStock(@PathVariable String donationReqId){
        String s = adminService.addBloodStock(Long.parseLong(donationReqId));
        return ResponseEntity.ok(s);
    }
}
