package com.tontineApp.tontine_manager.repository;

import com.tontineApp.tontine_manager.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {

    /**
     * Récupère tous les paiements d'un membre via ses cotisations
     * Chemin: Paiement → Cotisation → Membre
     */
    @Query("SELECT p FROM Paiement p WHERE p.cotisation.membre.id = :membreId ORDER BY p.datePaiement DESC")
    List<Paiement> findByMembreIdOrderByDatePaiementDesc(@Param("membreId") Integer membreId);

    /**
     * Récupère tous les paiements d'une tontine via les cotisations des membres
     * Chemin: Paiement → Cotisation → Membre → Tontine
     */
    @Query("SELECT p FROM Paiement p WHERE p.cotisation.membre.tontine.id = :tontineId ORDER BY p.datePaiement DESC")
    List<Paiement> findByTontineIdOrderByDatePaiementDesc(@Param("tontineId") Integer tontineId);

    /**
     * Récupère tous les paiements d'une cotisation spécifique
     */
    List<Paiement> findByCotisation_Id(Integer cotisationId);

    /**
     * Récupère les paiements validés d'un membre
     */
    @Query("SELECT p FROM Paiement p WHERE p.cotisation.membre.id = :membreId AND p.valide = true")
    List<Paiement> findValidesByMembreId(@Param("membreId") Integer membreId);

    /**
     * Compte les paiements validés d'un membre
     */
    @Query("SELECT COUNT(p) FROM Paiement p WHERE p.cotisation.membre.id = :membreId AND p.valide = true")
    Integer countValidesByMembreId(@Param("membreId") Integer membreId);

    /**
     * Compte les paiements en attente (valide = null) d'un membre
     */
    @Query("SELECT COUNT(p) FROM Paiement p WHERE p.cotisation.membre.id = :membreId AND p.valide IS NULL")
    Integer countEnAttenteByMembreId(@Param("membreId") Integer membreId);

    /**
     * Compte les paiements échoués (valide = false) d'un membre
     */
    @Query("SELECT COUNT(p) FROM Paiement p WHERE p.cotisation.membre.id = :membreId AND p.valide = false")
    Integer countEchouesByMembreId(@Param("membreId") Integer membreId);
}