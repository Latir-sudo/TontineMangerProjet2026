package com.tontineApp.tontine_manager.repository;

import com.tontineApp.tontine_manager.model.Tontine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TontineRepository extends JpaRepository<Tontine, Integer> {
}
