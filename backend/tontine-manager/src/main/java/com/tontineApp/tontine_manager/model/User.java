package com.tontineApp.tontine_manager.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="utilisateurs")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userid")
    private Integer id;

    @Column(name="prenom_utilisateur")
    private String prenomUtilisateur;
    private String email;
    private String telephone;
    @Column(name="userpassword")
    private String userPassword;
    @Column(name="date_inscription")
    private Date dateInscription;
    @Column(name="statut_compte")
    private String statutCompte;
    private String ville;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Membre> members;



}
