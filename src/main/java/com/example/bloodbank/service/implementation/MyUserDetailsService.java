package com.example.bloodbank.service.implementation;

import com.example.bloodbank.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.example.bloodbank.entity.User> byEmail = userRepo.findByEmail(username);
        if(byEmail.isEmpty()){
            throw new RuntimeException("User NOt found");
        }
        com.example.bloodbank.entity.User user = byEmail.get();
        String role = user.getRole();
        String[] split = role.split(",");
        List<SimpleGrantedAuthority> grantedAuthorityList = Arrays.stream(split).map(r -> new SimpleGrantedAuthority(r)).toList();
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(grantedAuthorityList)
                .build();
    }
}
