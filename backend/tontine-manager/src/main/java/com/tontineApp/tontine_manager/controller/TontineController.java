package com.tontineApp.tontine_manager.controller;

import com.tontineApp.tontine_manager.dto.*;
import com.tontineApp.tontine_manager.service.AdhesionService;
import com.tontineApp.tontine_manager.service.TontineFilterService;
import com.tontineApp.tontine_manager.service.TontineService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tontine")
public class TontineController {

    private final TontineService tontineService;
    private final AdhesionService adhesionService;
    private final TontineFilterService tontineFilterService;

    @GetMapping
    public List<TontineResponse> getTontines() {
        return tontineService.getAllTontines();
    }

    // MEMBRE ou ADMIN : voir une tontine spécifique
    @GetMapping("/{id}")
    public TontineResponse getTontine(@PathVariable("id") Integer idTontine) {
        return tontineService.getById(idTontine);
    }

    // TOUT LE MONDE AUTHENTIFIÉ : rechercher par région
    @GetMapping("/search")
    public List<TontineResponse> getByRegion(TontineFilter tontineFilter) {
        return tontineFilterService.TontineFilters(tontineFilter);
    }

    // TOUT UTILISATEUR CONNECTÉ : créer une tontine (devient admin)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public TontineResponse save(@RequestBody TontineRequest tontineRequest, Authentication authentication) {
        String email = authentication.getName();
        return tontineService.save(tontineRequest, email);
    }

    // ADMIN DE LA TONTINE : modifier
    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated() and @tontineSecurity.isAdmin(#authentication, #id)")
    public TontineResponse update(@RequestBody TontineRequest tontineRequest, @PathVariable("id") Integer id) {
        return tontineService.update(tontineRequest, id);
    }

    // ADMIN DE LA TONTINE : supprimer
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() and @tontineSecurity.isAdmin(#authentication, #id)")
    public void delete(@PathVariable("id") Integer id) {
        tontineService.delete(id);
    }

    // ADMIN DE LA TONTINE : voir les demandes d'adhésion
    @GetMapping("/adhesion")
    @PreAuthorize("isAuthenticated() and @tontineSecurity.isAdmin(#authentication, #idTontine)")
    public List<AdhesionResponse> getAttenteAdhesion(@RequestParam Integer idTontine) {
        return adhesionService.getAdhesionAttente(idTontine);
    }

    // TOUT LE MONDE : faire une demande d'adhésion
    @PostMapping("/adhesion")
    @PreAuthorize("isAuthenticated()")
    public AdhesionResponse save(@RequestBody AdhesionRequest adhesionRequest) {
        return adhesionService.save(adhesionRequest);
    }

    // ADMIN DE LA TONTINE : traiter une demande d'adhésion
    @PatchMapping("/adhesion")
    @PreAuthorize("isAuthenticated() and @tontineSecurity.isAdmin(#authentication, #idTontine)")
    public AdhesionResponse traiterAdhesion(
            @RequestParam Integer idUser,
            @RequestParam Integer idTontine,
            @RequestBody UpdateStatusDto nouveau,
            Authentication authentication) {
        return adhesionService.traiterAdhesion(idUser, nouveau, idTontine);
    }



    // GET /api/tontine/membre - Récupère les tontines du membre connecté
    @GetMapping("/mes-tontines")
    @PreAuthorize("isAuthenticated()")
    public List<TontineResponse> getMesTontines(Authentication authentication) {
        String email = authentication.getName();
        return tontineService.getMesTontines(email);
    }

}