package com.example.bloodbank.proxy;

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
public class BloodStockProxy {
    private Long id;
    @NotBlank(message = "BloodGroup cannot Blank")
    private String bloodGroup;
    @NotBlank(message = "AvailableUnits cannot Blank")
    private Integer availableUnits;
    @NotBlank(message = "LastUpdated cannot Blank")
    private LocalDateTime lastUpdated;
}
