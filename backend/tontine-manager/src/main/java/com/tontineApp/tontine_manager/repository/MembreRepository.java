package com.tontineApp.tontine_manager.repository;

import com.tontineApp.tontine_manager.model.Membre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MembreRepository{

   @Autowired
   MembreRepositoryInterface membreRepositoryInterface;

    private final JdbcTemplate jdbcTemplate;
    public MembreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getRoleByUserId(Integer userid,Integer idTontine){

        String sql = """
                select r.nom_role 
                from role r
                join membre m on r.id_role=m.id_role 
                where m.userid=? and id_tontine=?
                """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{idTontine}, String.class);
        }catch(EmptyResultDataAccessException e){
            return null;
        }




    }
}
