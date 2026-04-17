package com.tontineApp.tontine_manager.repository;

import com.tontineApp.tontine_manager.model.Membre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public interface TontineMembreRepository extends JpaRepository<Membre,Integer> {

    public List<Membre> findAllByUser_Id(Integer id);
    public List<Membre> findAllByTontine_Id(Integer id);


}
