package com.tontineApp.tontine_manager.repository;

import com.tontineApp.tontine_manager.enumeration.StatutAdhesion;
import com.tontineApp.tontine_manager.model.Adhesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdhesionRepository extends JpaRepository<Adhesion, Integer> {

    @Query("SELECT a FROM Adhesion a WHERE a.statut = :statut AND a.tontine.id = :tontineId")
    List<Adhesion> findByStatutAndTontine_Id(@Param("statut") StatutAdhesion statut, @Param("tontineId") Integer tontineId);
    
    @Query("SELECT a FROM Adhesion a WHERE a.user.id = :userId AND a.tontine.id = :tontineId")
    Optional<Adhesion> findByUser_IdAndTontine_Id(@Param("userId") Integer userId, @Param("tontineId") Integer tontineId);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Adhesion a WHERE a.user.id = :userId AND a.tontine.id = :tontineId AND a.statut = :statut")
    boolean existsByUser_IdAndTontine_IdAndStatut(@Param("userId") Integer userId, @Param("tontineId") Integer tontineId, @Param("statut") StatutAdhesion statut);
}