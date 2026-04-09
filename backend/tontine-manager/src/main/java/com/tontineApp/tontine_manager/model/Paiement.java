package com.tontineApp.tontine_manager.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="paiement")
public class Paiement {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_paiement")
    private Integer id;
    @Column(name="id_cotisation")
    private  Integer idCotisation;
    private Integer montant;
    @Column(name="date_paiement")
    private Date dateCotisation;
    @Column(name="mode_paiement")
    private String  modePaiement;
    private String reference;
    private Boolean valide;
}
