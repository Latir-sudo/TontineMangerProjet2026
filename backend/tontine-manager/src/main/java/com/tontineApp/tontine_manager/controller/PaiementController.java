package com.tontineApp.tontine_manager.controller;


import com.tontineApp.tontine_manager.dto.PaiementValide;
import com.tontineApp.tontine_manager.dto.UserCotisation;
import com.tontineApp.tontine_manager.model.User;
import com.tontineApp.tontine_manager.service.PaiementService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paiement")
@Validated
public class PaiementController {

    private PaiementService paiementService;

    public PaiementController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @GetMapping("/attentes")
    public List<UserCotisation> getCotisationEnAttente(){
        return paiementService.getCotisationAttente();
    }

    @PostMapping("/attentes")
    public void confirmerPaiementAttente(@RequestBody PaiementValide montant, @AuthenticationPrincipal User user){


        if(!user.getRole().isEqual('ADMIN'))


    }
}
