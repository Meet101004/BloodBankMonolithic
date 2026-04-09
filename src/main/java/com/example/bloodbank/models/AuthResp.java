package com.example.bloodbank.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResp {

    private String token;
    private String email;
    private String message;       // lockout message or success
    private long lockedForSeconds; // remaining lock duration in seconds
}
