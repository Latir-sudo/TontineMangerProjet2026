package com.tontineApp.tontine_manager.dto;

import com.tontineApp.tontine_manager.enumeration.StatutAdhesion;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdhesionResponse {

    @NotBlank
    private String prenomUser;
    private String nomUser;
    @NotBlank
    private String telephoneUser;
    private LocalDate dateAdhesion;
    private StatutAdhesion statut;
}
