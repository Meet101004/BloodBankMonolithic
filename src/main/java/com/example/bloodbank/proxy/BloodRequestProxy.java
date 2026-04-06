package com.example.bloodbank.proxy;

import com.example.bloodbank.entity.Hospital;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodRequestProxy {
    private Long id;
    @Valid
    private HospitalProxy hospital;
    @NotBlank(message = "BloodGroup cannot Blank")
    private String bloodGroup;
    @Min(value = 1,message = "min 1 unit require")
    private Integer quantity;
    private LocalDateTime requestDate;
    private Boolean status;
}
