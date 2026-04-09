package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.UserCotisation;
import com.tontineApp.tontine_manager.repository.PaiementRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaiementService {

    private PaiementRepository paiementRepository;

    // injection par constructeur
    public PaiementService(PaiementRepository paiementRepository) {

        this.paiementRepository = paiementRepository;
    }

    public List<UserCotisation> getCotisationAttente(){
        return paiementRepository.getCotisationAttente();
    }

    public void confirmerPaiement(int id,boolean valide){
        paiementRepository.updateStatutPaiement(id,valide);
    }
}
