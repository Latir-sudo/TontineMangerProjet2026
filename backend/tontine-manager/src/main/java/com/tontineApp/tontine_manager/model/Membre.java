package com.tontineApp.tontine_manager.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="membre")
public class Membre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_membre")
    private Integer idMembre;
    private Integer userid;
    @Column(name="id_tontine")
    private Integer idTontine;
    private Date dateAdhesion;
    @Column(name="preference_notification")
    private String preferenceNotification;


    @ManyToOne
    @JoinColumn(name="id_role")
    private Role role;

}
