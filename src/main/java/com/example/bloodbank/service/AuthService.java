package com.example.bloodbank.service;

import com.example.bloodbank.entity.User;
import com.example.bloodbank.models.AuthReq;
import com.example.bloodbank.models.AuthResp;
import com.example.bloodbank.proxy.UserProxy;

public interface AuthService {

    AuthResp login(AuthReq authReq);

    String register(UserProxy userProxy);

    String forget(AuthReq authReq);

}
