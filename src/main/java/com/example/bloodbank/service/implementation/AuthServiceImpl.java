package com.example.bloodbank.service.implementation;

import com.example.bloodbank.entity.DonorDetails;
import com.example.bloodbank.entity.Hospital;
import com.example.bloodbank.entity.User;
import com.example.bloodbank.exception.UserNotFoundException;
import com.example.bloodbank.models.AuthReq;
import com.example.bloodbank.models.AuthResp;
import com.example.bloodbank.proxy.DonorDetailsProxy;
import com.example.bloodbank.proxy.UserProxy;
import com.example.bloodbank.repository.HospitalRepo;
import com.example.bloodbank.repository.UserRepo;
import com.example.bloodbank.service.AuthService;
import com.example.bloodbank.utils.JwtUtil;
import com.example.bloodbank.utils.MapperHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MapperHelper mapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private HospitalRepo hospitalRepo;

    @Override
    public AuthResp login(AuthReq authReq) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= new UsernamePasswordAuthenticationToken(authReq.getEmail(),authReq.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if(authenticate.isAuthenticated()){
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(authReq.getEmail());myUserDetailsService.loadUserByUsername(authReq.getEmail());
            String s = jwtUtil.generateToken(userDetails);
            return AuthResp.builder()
                    .email(authReq.getEmail())
                    .token(s)
                    .build();
        }
        return null;
    }

    @Override
    public String register(UserProxy userProxy) {
        User user = mapper.proxyToEntityUser(userProxy);
        if( userRepo.existsByEmail(userProxy.getEmail())){
            return "Email Already Exist Use Different Email";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        if(user.getRole().equals("HOSPITAL")){
            user.setStatus(false);
        }
        if(user.getRole().equals("DONOR")){
            user.setStatus(false);
        }
        if(user.getRole().equals("ADMIN")){
            user.setStatus(true);
        }
        userRepo.save(user);
//        if(user.getRole().equals("HOSPITAL")) {
//            Hospital hospital = new Hospital();
//            hospital.setUser(user);
//
//            hospitalRepo.save(hospital);
//        }
        return "Registration Successfully Now Login!";
    }
    @Override
    public String forget(AuthReq authReq) {
        Optional<User> byEmail = userRepo.findByEmail(authReq.getEmail());
        if(byEmail.isEmpty()){
            throw new UserNotFoundException("User Not Found With this Email", HttpStatus.NOT_FOUND.value());
        }
        User user = byEmail.get();
        user.setPassword(encoder.encode(authReq.getPassword()));
        userRepo.save(user);
        return "Forget Password Successfully";
    }
}
