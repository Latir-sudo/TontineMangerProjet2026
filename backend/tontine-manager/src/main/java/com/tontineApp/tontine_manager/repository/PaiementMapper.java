package com.tontineApp.tontine_manager.repository;

import com.tontineApp.tontine_manager.dto.UserCotisation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaiementMapper implements RowMapper<UserCotisation> {

    @Override
    public UserCotisation mapRow(ResultSet rs , int rowNum) throws SQLException{
        return new UserCotisation(
                rs.getString("prenom_utilisateur"),
                rs.getInt("montant"),
                rs.getDate("date_paiement")!=null ? rs.getDate("date_paiement"): null,
                rs.getString("mode_paiement"),
                rs.getString("reference")
        );
    }
}
