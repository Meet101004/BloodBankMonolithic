package com.example.bloodbank.proxy;

import com.example.bloodbank.entity.User;
import jakarta.persistence.OneToOne;
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
public class DonorDetailsProxy {
    private Long id;
    @Valid
    private UserProxy user;
    @NotBlank(message = "BloodGroup cannot Blank")
    private String bloodGroup;
    private Integer age;
    @NotBlank(message = "Gender cannot Blank")
    private String gender;
    @NotBlank(message = "City cannot Blank")
    private String city;
    private LocalDateTime lastDonationDate;
    private Boolean available;
}
