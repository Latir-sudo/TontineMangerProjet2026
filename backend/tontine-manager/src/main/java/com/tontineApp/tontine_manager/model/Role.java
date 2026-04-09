package com.tontineApp.tontine_manager.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Entity
@Table (name="role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_role")
    private Integer id;

    @Column(name="nom_role")
    private String nomRole;


}
