package com.tontineApp.tontine_manager.repository;

import com.tontineApp.tontine_manager.model.Membre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MembreRepositoryInterface extends JpaRepository<Membre,Integer> {

    public boolean existsById(int id);
}
