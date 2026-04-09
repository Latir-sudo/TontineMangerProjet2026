package com.tontineApp.tontine_manager.controller;


import com.tontineApp.tontine_manager.dto.PaiementValide;
import com.tontineApp.tontine_manager.dto.UserCotisation;
import com.tontineApp.tontine_manager.model.User;
import com.tontineApp.tontine_manager.repository.MembreRepository;
import com.tontineApp.tontine_manager.repository.MembreRepositoryInterface;
import com.tontineApp.tontine_manager.service.AuthorizationService;
import com.tontineApp.tontine_manager.service.PaiementService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tontine/{id}/paiement")
@Validated
public class PaiementController {

    private final  PaiementService paiementService;
    private final MembreRepository  membreRepository;
    private final MembreRepositoryInterface membreRepositoryInterface;
    private final AuthorizationService  authorizationService;

    public PaiementController(PaiementService paiementService,MembreRepository  membreRepository,MembreRepositoryInterface membreRepositoryInterface, AuthorizationService authorizationService) {
        this.paiementService = paiementService;
        this.membreRepository = membreRepository;
        this.membreRepositoryInterface = membreRepositoryInterface;
        this.authorizationService = authorizationService;

    }

    @GetMapping("/attentes")
    public List<UserCotisation> getCotisationEnAttente(@AuthenticationPrincipal User user,@PathVariable("id") Integer idTontine){

       if(!authorizationService.isAdmin(user,idTontine)){
           throw new ResponseStatusException(HttpStatus.FORBIDDEN,"vous n'avez pas accès");
       }

        return paiementService.getCotisationAttente();
    }

    @PostMapping("/attentes")
    public void confirmerPaiementAttente(@PathVariable("id") Integer idTontine,@RequestBody PaiementValide paiement, @AuthenticationPrincipal User user){

       if(!authorizationService.isAdmin(user,idTontine)){
           throw new ResponseStatusException(HttpStatus.FORBIDDEN,"vous n'avez pas accès");
       }
        if(!membreRepositoryInterface.existsById(paiement.getIdMembre())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"membre non trouvé");
        }
        // vérifier que le membre appartient à la tontine et que le paiement n'as pas été validé



        paiementService.confirmerPaiement(paiement.getIdMembre(),true);

    }
}
