package com.tontineApp.tontine_manager.controller;

import com.tontineApp.tontine_manager.dto.PaiementHistoriqueResponse;
import com.tontineApp.tontine_manager.dto.PaiementRequest;
import com.tontineApp.tontine_manager.dto.PaiementResponse;
import com.tontineApp.tontine_manager.dto.PaiementStatsResponse;
import com.tontineApp.tontine_manager.service.PaiementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@AllArgsConstructor
public class PaiementController {

    private final PaiementService paiementService;

    /**
     * Récupère l'historique des paiements d'un membre
     * GET /api/paiements/membre/{membreId}/historique
     */
    @GetMapping("/membre/{membreId}/historique")
    @PreAuthorize("isAuthenticated() and @paiementSecurity.isOwner(#authentication, #membreId)")
    public ResponseEntity<List<PaiementHistoriqueResponse>> getHistoriqueByMembre(
            @PathVariable Integer membreId,
            Authentication authentication) {
        List<PaiementHistoriqueResponse> historique = paiementService.getHistoriqueByMembre(membreId);
        return ResponseEntity.ok(historique);
    }

    /**
     * Récupère l'historique des paiements d'une tontine
     * GET /api/paiements/tontine/{tontineId}/historique
     */
    @GetMapping("/tontine/{tontineId}/historique")
    @PreAuthorize("isAuthenticated() and @tontineSecurity.isMember(#authentication, #tontineId)")
    public ResponseEntity<List<PaiementHistoriqueResponse>> getHistoriqueByTontine(
            @PathVariable Integer tontineId,
            Authentication authentication) {
        List<PaiementHistoriqueResponse> historique = paiementService.getHistoriqueByTontine(tontineId);
        return ResponseEntity.ok(historique);
    }

    /**
     * Récupère tous les paiements d'une cotisation
     * GET /api/paiements/cotisation/{cotisationId}
     */
    @GetMapping("/cotisation/{cotisationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PaiementResponse>> getPaiementsByCotisation(
            @PathVariable Integer cotisationId,
            Authentication authentication) {
        List<PaiementResponse> paiements = paiementService.getPaiementsByCotisation(cotisationId);
        return ResponseEntity.ok(paiements);
    }

    /**
     * Crée un nouveau paiement pour une cotisation
     * POST /api/paiements/cotisation/{cotisationId}
     */
    @PostMapping("/cotisation/{cotisationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaiementResponse> createPaiement(
            @RequestBody PaiementRequest paiementRequest,
            @PathVariable Integer cotisationId,
            Authentication authentication) {
        PaiementResponse response = paiementService.createPaiement(paiementRequest, cotisationId);
        return ResponseEntity.ok(response);
    }

    /**
     * Valide un paiement (par un admin)
     * PUT /api/paiements/{paiementId}/valider
     */
    @PutMapping("/{paiementId}/valider")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaiementResponse> validerPaiement(
            @PathVariable Integer paiementId,
            Authentication authentication) {
        PaiementResponse response = paiementService.validerPaiement(paiementId);
        return ResponseEntity.ok(response);
    }

    /**
     * Rejette un paiement (par un admin)
     * PUT /api/paiements/{paiementId}/rejeter
     */
    @PutMapping("/{paiementId}/rejeter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaiementResponse> rejeterPaiement(
            @PathVariable Integer paiementId,
            Authentication authentication) {
        PaiementResponse response = paiementService.rejeterPaiement(paiementId);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupère les statistiques des paiements d'un membre
     * GET /api/paiements/membre/{membreId}/stats
     */
    @GetMapping("/membre/{membreId}/stats")
    @PreAuthorize("isAuthenticated() and @paiementSecurity.isOwner(#authentication, #membreId)")
    public ResponseEntity<PaiementStatsResponse> getStatsByMembre(
            @PathVariable Integer membreId,
            Authentication authentication) {
        PaiementStatsResponse stats = paiementService.getStatsByMembre(membreId);
        return ResponseEntity.ok(stats);
    }
}