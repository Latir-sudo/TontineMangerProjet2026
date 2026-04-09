package com.tontineApp.tontine_manager.repository;


import com.tontineApp.tontine_manager.dto.UserCotisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaiementRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<UserCotisation> getCotisationAttente(){

        String sql = """
                       SELECT u.prenom_utilisateur,p.montant,p.date_paiement,p.mode_paiement,p.reference
                       FROM utilisateurs u
                       JOIN membre m ON u.userid = m.userid
                       JOIN paiement p ON m.id_membre = p.id_membre
                       JOIN cotisation c ON p.id_cotisation = c.id_cotisation
                       WHERE p.valide = ?
                       """;
        return jdbcTemplate.query(
                sql,
                new PaiementMapper(),
                false
        );
    }

    // méthode pour définir le statut de paiement

    public int updateStatutPaiement(int id,Boolean valide){
        String sql= """
                UPDATE paiements p
                set valide = ? 
                from cotisation c 
                where c.id_cotisation = p.id_cotisation 
                and c.id_membre= ?
                """;
        return jdbcTemplate.update(sql,valide,id);
    }

}
