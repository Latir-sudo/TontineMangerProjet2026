package com.tontineApp.tontine_manager.controller;

import com.tontineApp.tontine_manager.dto.AdhesionResponse;
import com.tontineApp.tontine_manager.dto.UpdateStatusDto;
import com.tontineApp.tontine_manager.service.AdhesionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tontine/{idtontine}/adhesion")
public class AdhesionController {

    private final AdhesionService  adhesionService;
    public AdhesionController(AdhesionService adhesionService) {
        this.adhesionService = adhesionService;
    }

    @GetMapping("/attentes")
    public List<AdhesionResponse> getAttenteAdhesion(@PathVariable("idtontine") Integer id) {
        return adhesionService.getAdhesionAttente(id);
    }

    @PatchMapping ("/{id}")
    public AdhesionResponse traiterAdhesion(@PathVariable("id") Integer id, @PathVariable("idtontine") Integer idTontine, @RequestBody UpdateStatusDto nouveau){
        return adhesionService.traiterAdhesion(id,nouveau,idTontine);
    }


}
