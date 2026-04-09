package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.model.User;
import com.tontineApp.tontine_manager.repository.MembreRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final MembreRepository membreRepository;
    public AuthorizationService(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    public boolean isAdmin(User user, int idTontine) {
        String role =membreRepository.getRoleByUserId(user.getId(), idTontine);
        return "ADMIN".equalsIgnoreCase(role);
    }

}
