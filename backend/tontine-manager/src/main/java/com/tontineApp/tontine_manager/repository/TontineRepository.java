package com.tontineApp.tontine_manager.repository;


import com.tontineApp.tontine_manager.model.Tontine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TontineRepository extends JpaRepository<Tontine,Integer> {

    public List<Tontine> findByDateCreation(LocalDate dateCreation);
    public List<Tontine> findByFrequence(String frequence);
    public List<Tontine> findByMontant(Integer montant);
    public List<Tontine> findByNomTontine(String nomTontine);
}
