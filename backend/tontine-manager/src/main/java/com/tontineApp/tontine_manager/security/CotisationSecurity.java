package com.tontineApp.tontine_manager.security;

import com.tontineApp.tontine_manager.model.Cotisation;
import com.tontineApp.tontine_manager.model.Users;
import com.tontineApp.tontine_manager.repository.CotisationRepository;
import com.tontineApp.tontine_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("cotisationSecurity")
@RequiredArgsConstructor
public class CotisationSecurity {

    private final CotisationRepository cotisationRepository;
    private final UserRepository userRepository;
    private final TontineSecurity tontineSecurity;

    private Users getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }

    // Vérifier si l'utilisateur est le propriétaire de la cotisation
    public boolean isOwner(Authentication authentication, Integer cotisationId) {
        Users user = getCurrentUser(authentication);
        if (user == null) return false;

        Cotisation cotisation = cotisationRepository.findById(cotisationId).orElse(null);
        if (cotisation == null) return false;

        return cotisation.getMembre().getUser().getId().equals(user.getId());
    }

    // Vérifier si l'utilisateur peut voir la cotisation (admin de la tontine ou propriétaire)
    public boolean canView(Authentication authentication, Integer cotisationId) {
        Users user = getCurrentUser(authentication);
        if (user == null) return false;

        Cotisation cotisation = cotisationRepository.findById(cotisationId).orElse(null);
        if (cotisation == null) return false;

        Integer tontineId = cotisation.getMembre().getTontine().getId();

        return tontineSecurity.isAdmin(authentication, tontineId) || isOwner(authentication, cotisationId);
    }
}