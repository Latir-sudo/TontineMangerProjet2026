package com.tontineApp.tontine_manager.controller;

import com.tontineApp.tontine_manager.dto.CotisationRequest;
import com.tontineApp.tontine_manager.dto.CotisationResponse;
import com.tontineApp.tontine_manager.service.CotisationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cotisations")
@AllArgsConstructor
public class CotisationController {

    private final CotisationService cotisationService;

    // MEMBRE : voir ses propres cotisations (l'idUser doit être le sien)
    @GetMapping("/user/{id}/tontine/{idTontine}")
    @PreAuthorize("isAuthenticated() and @cotisationSecurity.isOwner(authentication, #idUser, #idTontine)")
    public ResponseEntity<List<CotisationResponse>> getAllCotisations(
            @PathVariable("id") Integer idUser,
            @PathVariable("idTontine") Integer idTontine,
            Authentication authentication) {
        List<CotisationResponse> cotisations = cotisationService.getCotisations(idUser, idTontine);
        return ResponseEntity.ok(cotisations);
    }

    // MEMBRE : créer une cotisation (l'idUser doit être le sien)
    @PostMapping("/user/{id}/tontine/{idTontine}")
    @PreAuthorize("isAuthenticated() and @tontineSecurity.isMember(authentication, #idTontine)")
    public ResponseEntity<CotisationResponse> saveCotisation(
            @RequestBody CotisationRequest cotisationRequest,
            @PathVariable("id") Integer userId,
            @PathVariable("idTontine") Integer idTontine,
            Authentication authentication) {
        CotisationResponse response = cotisationService.createCotisation(cotisationRequest, userId, idTontine);
        return ResponseEntity.ok(response);
    }
}