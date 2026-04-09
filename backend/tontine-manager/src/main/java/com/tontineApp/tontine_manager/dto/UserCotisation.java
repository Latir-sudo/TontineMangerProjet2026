package com.tontineApp.tontine_manager.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data

public class UserCotisation {

    private String prenomUtilisateur;
    private Integer montant;
    private Date datePaiement;
    private String modePaiement;
    private String reference;

    // constructeurs;

    public UserCotisation(String prenomUtilisateur, Integer montant, Date dateCotisation, String modeCotisation, String reference) {
        this.prenomUtilisateur = prenomUtilisateur;
        this.montant = montant;
        this.datePaiement = dateCotisation;
        this.modePaiement = modeCotisation;
        this.reference = reference;

    }
}
