package com.example.bloodbank.proxy;

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
public class DonateRequest {
    @NotBlank(message = "BloodGroup cannot Blank")
    private String bloodGroup;
    @NotBlank(message = "Units cannot Blank")
    private Integer units;
}
