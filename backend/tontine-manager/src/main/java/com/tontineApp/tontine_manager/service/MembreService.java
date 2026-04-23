package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.AjoutUtilisateurTontineRequest;
import com.tontineApp.tontine_manager.model.Membre;
import com.tontineApp.tontine_manager.model.User;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.repository.MembreRepository;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import com.tontineApp.tontine_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MembreService {

    private final UserRepository userRepository;
    private final TontineRepository tontineRepository;
    private final MembreRepository membreRepository;

    @Transactional
    public Membre ajouterUtilisateurATontine(AjoutUtilisateurTontineRequest request) {
        // 1. Vérifier que la tontine existe
        Tontine tontine = tontineRepository.findById(request.getTontineId())
                .orElseThrow(() -> new RuntimeException("Tontine non trouvée"));

        // 2. Trouver ou créer l'utilisateur
        User user;

            // Recherche par téléphone
            user = userRepository.findByTelephone(request.getTelephone())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec ce numéro"));


        // 3. Vérifier qu'il n'est pas déjà membre
        boolean dejaMembre = membreRepository.existsByTontineAndUser(tontine, user);
        if (dejaMembre) {
            throw new RuntimeException("Cet utilisateur est déjà membre de cette tontine");
        }

        // 4. Créer le membre
        Membre membre = new Membre();
        membre.setTontine(tontine);
        membre.setUser(user);
        membre.setDateAdhesion(LocalDateTime.now());
        membre = membreRepository.save(membre);

        // 5. Notification


        return membre;
    }
}