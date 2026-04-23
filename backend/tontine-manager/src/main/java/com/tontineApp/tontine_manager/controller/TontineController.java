package com.tontineApp.tontine_manager.controller;

import com.tontineApp.tontine_manager.dto.AdhesionResponse;
import com.tontineApp.tontine_manager.dto.TontineRequest;
import com.tontineApp.tontine_manager.dto.UpdateStatusDto;
import com.tontineApp.tontine_manager.service.AdhesionService;
import com.tontineApp.tontine_manager.service.TontineService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tontine")
public class TontineController {

    private final TontineService tontineService;
    private final AdhesionService adhesionService;

    @PostMapping("/")
    public TontineRequest createTontine(@Valid @RequestBody TontineRequest tontineRequest){
        return tontineService.save(tontineRequest);
    }
    @GetMapping("/")
    public List<TontineRequest> getAllTontine(){
        return tontineService.getAllTontine();
    }
    @GetMapping("/{id}")
    public TontineRequest getTontine(@PathVariable Integer id){
        return tontineService.getTontineById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id){
        tontineService.delete(id);
    }

    @PatchMapping("/{id}")
    public TontineRequest updateTontine(@PathVariable("id") Integer id,@RequestBody TontineRequest tontineRequest){
        return tontineService.update(tontineRequest,id);
    }



    @GetMapping("/{id}/adhesion/attentes")
    public List<AdhesionResponse> getAttenteAdhesion(@PathVariable("id") Integer id) {
        return adhesionService.getAdhesionAttente(id);
    }

    @PatchMapping ("/{idtontine}/adhesion/{idUser}")
    public AdhesionResponse traiterAdhesion(@PathVariable("idUser") Integer id, @PathVariable("idtontine") Integer idTontine, @RequestBody UpdateStatusDto nouveau){
        return adhesionService.traiterAdhesion(id,nouveau,idTontine);
    }
}
