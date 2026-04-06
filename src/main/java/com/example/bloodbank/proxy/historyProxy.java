package com.example.bloodbank.proxy;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class historyProxy {

    @NotBlank(message = "DonationDate cannot Blank")
    private LocalDateTime donationDate;
    @NotBlank(message = "Quantity cannot Blank")
    private Integer quantity;
    @NotBlank(message = "BloodGroup cannot Blank")
    private String bloodGroup;
    private Boolean status;

}
