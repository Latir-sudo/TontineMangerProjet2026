package com.tontineApp.tontine_manager.dto;
import com.tontineApp.tontine_manager.model.Role;
import jakarta.validation.constraints.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserRequest {
    @NotBlank
    private String prenom;
    @NotBlank
    private String nom;
    @Email
    private String email;
    @NotBlank
    private String telephone;
    @Size(min=8)
    private String UserPassword;
    private LocalDate dateInscription;
    @NotBlank
    private String ville;
    List<Role> roles;
}
