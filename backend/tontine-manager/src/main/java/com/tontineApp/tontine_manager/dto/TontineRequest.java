package com.tontineApp.tontine_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TontineRequest {
    private String nomTontine;
    private String frequence;
    private Integer montant;
    private LocalDate dateCreation;
    private String descriptionTontine;
    private String politiqueTontine;
}
