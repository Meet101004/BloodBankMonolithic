package com.example.bloodbank.proxy;

import com.example.bloodbank.entity.DonorDetails;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationProxy {
    private Long id;
    @Valid
    private DonorDetailsProxy donorDetails;
    @NotBlank(message = "DonationDate cannot Blank")
    private LocalDateTime donationDate;
    @NotBlank(message = "Quantity cannot Blank")
    private Integer quantity;
    @NotBlank(message = "BloodGroup cannot Blank")
    private String bloodGroup;
    private Boolean status;
}
