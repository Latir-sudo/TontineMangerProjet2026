package com.tontineApp.tontine_manager.controller;


import com.tontineApp.tontine_manager.dto.AjoutUtilisateurTontineRequest;
import com.tontineApp.tontine_manager.dto.MembreRequest;
import com.tontineApp.tontine_manager.model.Membre;
import com.tontineApp.tontine_manager.service.MembreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/membres")
@RequiredArgsConstructor
public class MembreController {

    private final MembreService membreService;

    @PostMapping
    public ResponseEntity<?> ajouterUtilisateurATontine(@RequestBody MembreRequest request) {
        try {
            System.out.println("bonjour");
            MembreRequest nouveauMembre = membreService.ajouterUtilisateurATontine(request);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Utilisateur "+ nouveauMembre.getIdUser()+" ajouté avec succès à la tontine");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("status", "error");
            return ResponseEntity.badRequest().body(error);
        }
    }
}