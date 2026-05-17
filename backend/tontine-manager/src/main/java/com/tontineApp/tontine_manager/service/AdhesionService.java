package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.AdhesionRequest;
import com.tontineApp.tontine_manager.dto.AdhesionResponse;
import com.tontineApp.tontine_manager.dto.UpdateStatusDto;
import com.tontineApp.tontine_manager.enumeration.StatutAdhesion;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.exception.UnAuthorizedException;
import com.tontineApp.tontine_manager.mapper.AdhesionMapper;
import com.tontineApp.tontine_manager.model.Adhesion;
import com.tontineApp.tontine_manager.model.Membre;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.model.Users;
import com.tontineApp.tontine_manager.repository.AdhesionRepository;
import com.tontineApp.tontine_manager.repository.MembreRepository;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import com.tontineApp.tontine_manager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AdhesionService {

    private final AdhesionRepository adhesionRepository;
    private final TontineRepository tontineRepository;
    private final UserRepository userRepository;
    private final MembreRepository membreRepository;
    private final AdhesionMapper adhesionMapper;

    public List<AdhesionResponse> getAdhesionAttente(Integer idTontine) {
        log.info("Récupération des adhésions en attente pour la tontine {}", idTontine);

        if (!tontineRepository.existsById(idTontine)) {
            throw new RessourceNotFoundException("Tontine " + idTontine + " non trouvée");
        }

        List<Adhesion> adhesions = adhesionRepository.findByStatutAndTontine_Id(StatutAdhesion.ATTENTE, idTontine);
        log.info("{} adhésion(s) en attente trouvée(s)", adhesions.size());

        return adhesions.stream()
                .map(adhesionMapper::toAdhesionResponse)
                .toList();
    }

    @Transactional
    public AdhesionResponse traiterAdhesion(Integer idUser, UpdateStatusDto nouveau, Integer idTontine) {
        log.info("=== TRAITEMENT ADHESION ===");
        log.info("idUser: {}, idTontine: {}, statut reçu: {}", idUser, idTontine, nouveau.getStatut());

        // 1. Récupérer l'adhésion
        Adhesion adhesion = adhesionRepository.findByUser_IdAndTontine_Id(idUser, idTontine)
                .orElseThrow(() -> new RessourceNotFoundException("Adhésion non trouvée"));

        log.info("Adhésion trouvée - ID: {}, Statut actuel: {}", adhesion.getId(), adhesion.getStatut());

        // 2. Vérifier que l'adhésion est en attente
        if (adhesion.getStatut() != StatutAdhesion.ATTENTE) {
            throw new UnAuthorizedException("Cette demande a déjà été traitée");
        }

        // 3. Mettre à jour le statut
        if ("ACCEPTEE".equals(nouveau.getStatut())) {
            adhesion.setStatut(StatutAdhesion.ACCEPTEE);
        } else if ("REJETEE".equals(nouveau.getStatut())) {
            adhesion.setStatut(StatutAdhesion.REJETEE);
        } else {
            throw new IllegalArgumentException("Statut invalide: " + nouveau.getStatut());
        }

        Adhesion saved = adhesionRepository.save(adhesion);
        log.info("Adhésion sauvegardée - Nouveau statut: {}", saved.getStatut());

        // 4. Si acceptée, créer le membre
        if ("ACCEPTEE".equals(nouveau.getStatut())) {
            log.info("Création du membre...");

            boolean dejaMembre = membreRepository.existsByTontine_IdAndUser_Id(idTontine, idUser);

            if (!dejaMembre) {
                Tontine tontine = tontineRepository.findById(idTontine)
                        .orElseThrow(() -> new RessourceNotFoundException("Tontine non trouvée"));
                Users user = userRepository.findById(idUser)
                        .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé"));

                Membre membre = new Membre();
                membre.setTontine(tontine);
                membre.setUser(user);
                membre.setDateAdhesion(LocalDate.now());
                membreRepository.save(membre);

                tontine.setNombreMembres(tontine.getNombreMembres() + 1);
                tontineRepository.save(tontine);

                log.info("Membre créé avec succès");
            }
        }

        return adhesionMapper.toAdhesionResponse(saved);
    }

    public AdhesionResponse save(AdhesionRequest request, Integer idTontine) {
        log.info("Création d'une demande d'adhésion pour user={}, tontine={}", request.getIdUser(), idTontine);

        // Vérifier la tontine
        Tontine tontine = tontineRepository.findById(idTontine)
                .orElseThrow(() -> new RessourceNotFoundException("Tontine non trouvée"));

        // Vérifier l'utilisateur
        Users user = userRepository.findById(request.getIdUser())
                .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé"));

        // Vérifier si déjà membre
        boolean dejaMembre = membreRepository.existsByTontine_IdAndUser_Id(idTontine, request.getIdUser());
        if (dejaMembre) {
            throw new UnAuthorizedException("Vous êtes déjà membre");
        }

        // Vérifier si demande en attente
        boolean demandeExistante = adhesionRepository.existsByUser_IdAndTontine_IdAndStatut(
                request.getIdUser(), idTontine, StatutAdhesion.ATTENTE);
        if (demandeExistante) {
            throw new UnAuthorizedException("Demande déjà en attente");
        }

        // Créer l'adhésion
        Adhesion adhesion = new Adhesion();
        adhesion.setUser(user);
        adhesion.setTontine(tontine);
        adhesion.setDateAdhesion(LocalDate.now());
        adhesion.setStatut(StatutAdhesion.ATTENTE);

        Adhesion saved = adhesionRepository.save(adhesion);
        log.info("Demande créée avec succès, id={}", saved.getId());

        return adhesionMapper.toAdhesionResponse(saved);
    }
}