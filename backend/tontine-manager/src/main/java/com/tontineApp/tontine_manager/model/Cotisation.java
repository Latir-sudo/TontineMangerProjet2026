package com.tontineApp.tontine_manager.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="cotisation")

public class Cotisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_cotisation")
    private Integer id;
    @Column(name="montant_total")
    private Double montantTotal;
    @Column(name="statut_cotisation")
    private String statut;
    @Column(name="date_cotisation")
    private String date;


    @OneToMany(mappedBy = "cotisation")
    private List<Paiement> paiements=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="membre_id")
    private Membre membre;

}
