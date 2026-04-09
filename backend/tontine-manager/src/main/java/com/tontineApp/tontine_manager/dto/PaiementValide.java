package com.tontineApp.tontine_manager.dto;


import lombok.Data;

@Data
public class PaiementValide {

    private Integer idMembre;
    private Integer montant;

    public PaiementValide(Integer idMembre, Integer montant) {
        this.idMembre = idMembre;
        this.montant = montant;
    }
}
