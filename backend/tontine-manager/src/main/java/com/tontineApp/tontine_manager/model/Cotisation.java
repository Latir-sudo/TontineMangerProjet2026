package com.tontineApp.tontine_manager.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="cotisation")

public class Cotisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_cotisation")
    private Integer id;
    @Column(name="id_membre")
    private Integer membre;
    @Column(name="montant_total")
    private Double montantTotal;
    @Column(name="statut_cotisation")
    private String statut;
    @Column(name="date_cotisation")
    private String date;

}
