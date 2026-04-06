package com.example.bloodbank.proxy;

import jakarta.persistence.Column;
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
public class UserProxy {
    private Long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @Email(message = "Invalid Email Format")
    @NotBlank(message = "Email cannot Blank")
    @NotNull(message = "Email cannot null")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min=3,max=20,message = "Password is in between 3 to 20 character")
    private String password;

    @NotBlank(message = "Role cannot Blank")
    @NotNull(message = "Role cannot null")
    private String role;

    @NotBlank(message = "Phone cannot Blank")
    private String phone;
    private Boolean status;
}
