package com.example.bloodbank.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserNotFoundException extends RuntimeException{

    private String errMsg;
    private Integer errCode;
    private LocalDateTime dateTime;

    public UserNotFoundException(String msg,Integer errCode){
        this.errMsg=msg;
        this.errCode=errCode;
        this.dateTime=LocalDateTime.now();
    }
}
