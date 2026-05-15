package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.PaiementHistoriqueResponse;
import com.tontineApp.tontine_manager.dto.PaiementRequest;
import com.tontineApp.tontine_manager.dto.PaiementResponse;
import com.tontineApp.tontine_manager.dto.PaiementStatsResponse;
import com.tontineApp.tontine_manager.enumeration.StatutCotisation;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.PaiementMapper;
import com.tontineApp.tontine_manager.model.Cotisation;
import com.tontineApp.tontine_manager.model.Paiement;
import com.tontineApp.tontine_manager.repository.CotisationRepository;
import com.tontineApp.tontine_manager.repository.MembreRepository;
import com.tontineApp.tontine_manager.repository.PaiementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.tontineApp.tontine_manager.enumeration.StatutCotisation.*;

@Service
@AllArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final CotisationRepository cotisationRepository;
    private final PaiementMapper paiementMapper;
    private final MembreRepository membreRepository;
    /**
     * Récupère l'historique des paiements d'un membre
     */
    public List<PaiementHistoriqueResponse> getHistoriqueByMembre(Integer membreId) {
        List<Paiement> paiements = paiementRepository.findByMembreIdOrderByDatePaiementDesc(membreId);
        return paiements.stream()
                .map(paiementMapper::toHistoriqueResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupère l'historique des paiements d'une tontine
     */
    public List<PaiementHistoriqueResponse> getHistoriqueByTontine(Integer tontineId) {
        List<Paiement> paiements = paiementRepository.findByTontineIdOrderByDatePaiementDesc(tontineId);
        return paiements.stream()
                .map(paiementMapper::toHistoriqueResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les paiements d'une cotisation
     */
    public List<PaiementResponse> getPaiementsByCotisation(Integer cotisationId) {
        List<Paiement> paiements = paiementRepository.findByCotisation_Id(cotisationId);
        return paiements.stream()
                .map(paiementMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Crée un nouveau paiement pour une cotisation
     */
    @Transactional
    public PaiementResponse createPaiement(PaiementRequest request, Integer cotisationId) {
        Cotisation cotisation = cotisationRepository.findById(cotisationId)
                .orElseThrow(() -> new RessourceNotFoundException("Cotisation non trouvée avec l'id: " + cotisationId));

        Paiement paiement = new Paiement();
        paiement.setMontant(request.getMontant());
        paiement.setDatePaiement(request.getDatePaiement() != null ? request.getDatePaiement() : new Date());
        paiement.setModePaiement(request.getModePaiement());
        paiement.setReference(request.getReference());
        paiement.setValide(false);
        paiement.setCotisation(cotisation);

        Paiement saved = paiementRepository.save(paiement);

        // Mettre à jour le statut de la cotisation
        updateCotisationStatut(cotisation);

        return paiementMapper.toResponse(saved);
    }

    /**
     * Valide un paiement (par un admin)
     */
    @Transactional
    public PaiementResponse validerPaiement(Integer paiementId) {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new RessourceNotFoundException("Paiement non trouvé avec l'id: " + paiementId));

        paiement.setValide(true);
        Paiement saved = paiementRepository.save(paiement);

        // Mettre à jour le statut de la cotisation
        updateCotisationStatut(paiement.getCotisation());

        return paiementMapper.toResponse(saved);
    }

    /**
     * Rejette un paiement (par un admin)
     */
    @Transactional
    public PaiementResponse rejeterPaiement(Integer paiementId) {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new RessourceNotFoundException("Paiement non trouvé avec l'id: " + paiementId));

        paiement.setValide(false);
        Paiement saved = paiementRepository.save(paiement);

        return paiementMapper.toResponse(saved);
    }

    /**
     * Récupère les statistiques des paiements d'un membre
     */
    public PaiementStatsResponse getStatsByMembre(Integer membreId) {
        // Récupérer toutes les cotisations du membre
        List<Cotisation> cotisations = cotisationRepository.findByMembre_Id(membreId);
        Long totalAttendu = cotisations.stream().mapToLong(Cotisation::getMontant).sum();

        // Récupérer les paiements validés
        List<Paiement> paiementsValides = paiementRepository.findValidesByMembreId(membreId);
        Long totalPaye = paiementsValides.stream().mapToLong(Paiement::getMontant).sum();

        Integer paiementsReussis = paiementRepository.countValidesByMembreId(membreId);
        Integer paiementsEnAttente = paiementRepository.countEnAttenteByMembreId(membreId);
        Integer paiementsEchoues = paiementRepository.countEchouesByMembreId(membreId);

        Double tauxCompletude = totalAttendu > 0 ? (totalPaye.doubleValue() / totalAttendu.doubleValue()) * 100 : 0;

        return new PaiementStatsResponse(
                totalPaye, totalAttendu, paiementsReussis, paiementsEnAttente, paiementsEchoues, tauxCompletude
        );
    }

    /**
     * Met à jour le statut d'une cotisation en fonction des paiements validés
     */
    private void updateCotisationStatut(Cotisation cotisation) {
        List<Paiement> paiements = paiementRepository.findByCotisation_Id(cotisation.getId());

        Long totalPaye = paiements.stream()
                .filter(p -> p.getValide() != null && p.getValide())
                .mapToLong(Paiement::getMontant)
                .sum();

        StatutCotisation newStatut;

        if (totalPaye >= cotisation.getMontant()) {
            newStatut = COMPLET;
        } else if (totalPaye > 0) {
            newStatut = PARTIEL;
        } else {
            newStatut = EN_ATTENTE;
        }

        cotisation.setStatut(newStatut);
        cotisationRepository.save(cotisation);
    }

    public boolean isMembreOwner(String email, Integer membreId) {
        // Chercher le membre par son ID
        return membreRepository.findById(membreId)
                .map(membre -> {
                    // Vérifier si l'email de l'utilisateur correspond
                    if (membre.getUser() != null) {
                        return membre.getUser().getEmail().equals(email);
                    }
                    return false;
                })
                .orElse(false);
    }
}