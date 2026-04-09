package com.example.bloodbank.service;

import com.example.bloodbank.models.AuthReq;
import com.example.bloodbank.models.AuthResp;
import com.example.bloodbank.models.ForgotPasswordRequest;
import com.example.bloodbank.models.ResetPasswordRequest;
import com.example.bloodbank.proxy.UserProxy;

public interface AuthService {

    AuthResp login(AuthReq authReq);

    String register(UserProxy userProxy);

    String forget(AuthReq authReq);

    String forgotPassword(ForgotPasswordRequest request);

    String resetPassword(ResetPasswordRequest request);

}
