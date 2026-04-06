package com.example.bloodbank.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Response {
    private String errMsg;
    private Integer errCode;
    private String path;
    private LocalDateTime dateTime;
}
