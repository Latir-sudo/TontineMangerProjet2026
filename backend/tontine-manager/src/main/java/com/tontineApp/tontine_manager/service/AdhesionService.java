package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.AdhesionRequest;
import com.tontineApp.tontine_manager.dto.AdhesionResponse;
import com.tontineApp.tontine_manager.dto.MembreRequest;
import com.tontineApp.tontine_manager.dto.UpdateStatusDto;
import com.tontineApp.tontine_manager.enumeration.StatutAdhesion;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.exception.UnAuthorizedException;
import com.tontineApp.tontine_manager.mapper.AdhesionMapper;
import com.tontineApp.tontine_manager.mapper.MembreMapper;
import com.tontineApp.tontine_manager.model.Adhesion;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.repository.AdhesionRepository;
import com.tontineApp.tontine_manager.repository.MembreRepository;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tontineApp.tontine_manager.enumeration.StatutAdhesion.ACCEPTEE;

@Slf4j
@Service
@AllArgsConstructor
public class AdhesionService {

    private final AdhesionRepository adhesionRepository;
    private final TontineRepository tontineRepository;
    private final MembreMapper membreMapper;
    private final MembreRepository membreRepository;
    private final AdhesionMapper adhesionMapper;

    public List<AdhesionResponse> getAdhesionAttente(Integer idTontine) {
        // débogage pour voir si mon controller a un problème ou non
        System.out.println("adhesion en attente en cours");
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

    // ✅ Traiter une adhésion (approuver/rejeter)
    @Transactional
    public AdhesionResponse traiterAdhesion(Integer idUser, UpdateStatusDto nouveau, Integer idTontine) {
        log.info("Traitement de l'adhésion pour user={}, tontine={}, nouveau statut={}", idUser, idTontine, nouveau.getStatut());

        Adhesion adhesion = adhesionRepository.findByUser_IdAndTontine_Id(idUser, idTontine)
                .orElseThrow(() -> new RessourceNotFoundException(
                        String.format("Adhésion non trouvée pour l'utilisateur %d dans la tontine %d", idUser, idTontine)
                ));

        if (nouveau == null || nouveau.getStatut() == null) {
            throw new IllegalArgumentException("Le statut ne peut pas être null");
        }

        // vérifier si l'utilisateur n'est pas déjà membre de la tontine
        if((adhesionRepository.existsByUser_idAndTontine_IdAndStatut(idUser,idTontine,ACCEPTEE))){
            throw new UnAuthorizedException("utilisateur déjà membre");
        }

        // Mettre à jour le statut
        adhesion.setStatut(nouveau.getStatut());
        if (nouveau.getDate() != null) {
            adhesion.setDateAdhesion(nouveau.getDate());
        }

        // Si acceptée, créer le membre
        if (ACCEPTEE.equals(nouveau.getStatut())) {
            log.info("Création du membre pour l'utilisateur {} dans la tontine {}", idUser, idTontine);

            MembreRequest membreRequest = new MembreRequest();
            membreRequest.setIdTontine(idTontine);
            membreRequest.setIdUser(idUser);
            membreRepository.save(membreMapper.toMembre(membreRequest));
        }

        Adhesion saved = adhesionRepository.save(adhesion);
        log.info("Adhésion traitée avec succès, nouveau statut: {}", saved.getStatut());

        return adhesionMapper.toAdhesionResponse(saved);
    }

    // ✅ Créer une nouvelle demande d'adhésion
    public AdhesionResponse save(AdhesionRequest request, Integer idTontine) {
        log.info("Création d'une demande d'adhésion pour user={}, tontine={}", request.getIdUser(), idTontine);

        if (request == null) {
            throw new IllegalArgumentException("La requête d'adhésion ne peut pas être null");
        }

        Tontine tontine = tontineRepository.findById(idTontine)
                .orElseThrow(() -> new RessourceNotFoundException("Tontine " + idTontine + " non trouvée"));

        Adhesion adhesion = adhesionMapper.toAdhesion(request);
        adhesion.setTontine(tontine);

        Adhesion saved = adhesionRepository.save(adhesion);
        log.info("Demande d'adhésion créée avec succès, id={}", saved.getId());

        return adhesionMapper.toAdhesionResponse(saved);
    }
}