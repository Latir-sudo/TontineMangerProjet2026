package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.AjoutUtilisateurTontineRequest;
import com.tontineApp.tontine_manager.dto.MembreRequest;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.MembreMapper;
import com.tontineApp.tontine_manager.model.Membre;
import com.tontineApp.tontine_manager.model.User;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.repository.MembreRepository;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import com.tontineApp.tontine_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MembreService {

    private final UserRepository userRepository;
    private final TontineRepository tontineRepository;
    private final MembreRepository membreRepository;
    private final MembreMapper membreMapper;

    @Transactional
    public MembreRequest ajouterUtilisateurATontine(MembreRequest request) {
        // 1. Vérifier que la tontine existe
        Tontine tontine = tontineRepository.findById(request.getIdTontine()).orElseThrow(()->new RessourceNotFoundException("tontine non trouvée"));

            // Recherche par téléphone
        User  user = userRepository.findById(request.getIdUser())
                    .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec ce numéro"));

        // 3. Vérifier qu'il n'est pas déjà membre

        boolean dejaMembre = membreRepository.existsByTontineAndUser(tontine, user);
        if (dejaMembre) {
            throw new IllegalArgumentException("Cet utilisateur est déjà membre de cette tontine");
        }

        // 4. Créer le membre
       Membre membre = membreMapper.toMembre(request);
        return membreMapper.toMembreRequest(membreRepository.save(membre));

    }


}