package com.tontineApp.tontine_manager.dto;

import lombok.Data;

@Data
public class AjoutUtilisateurTontineRequest {
    private Integer tontineId;
    private String telephone;
    private String nom;
    private String prenom;
}
