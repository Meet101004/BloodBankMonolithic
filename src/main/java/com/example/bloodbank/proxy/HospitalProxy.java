package com.example.bloodbank.proxy;

import com.example.bloodbank.entity.User;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalProxy {
    private Long id;

    @NotBlank(message = "HospitalName cannot Blank")
    @NotNull(message = "HospitalName cannot null")
    private String hospitalName;

    @Valid
    private UserProxy user;

    @NotBlank(message = "Address cannot Blank")
    @Size(min=3,max=20,message = "Address is in between 3 to 20 character")
    private String address;

    @NotBlank(message = "ContactNum cannot Blank")
    private String contactNum;

    @NotBlank(message = "LicenseNum cannot Blank")
    private String licenceNumber;
}
