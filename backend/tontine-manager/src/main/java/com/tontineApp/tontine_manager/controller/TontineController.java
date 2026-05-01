package com.tontineApp.tontine_manager.controller;

import com.tontineApp.tontine_manager.dto.*;
import com.tontineApp.tontine_manager.service.AdhesionService;
import com.tontineApp.tontine_manager.service.TontineFilterService;
import com.tontineApp.tontine_manager.service.TontineService;
import lombok.AllArgsConstructor;
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
    public List<TontineResponse> getTontines(){
        return tontineService.getAllTontines();
    }
    @GetMapping("/{id}")
    public TontineResponse getTontine(@PathVariable("id") Integer idTontine){
        return tontineService.getById(idTontine);
    }
    @GetMapping("/search")
    public List<TontineResponse> getByRegion(TontineFilter tontineFilter){
        return tontineFilterService.TontineFilters(tontineFilter);
    }
    @PostMapping
    public TontineResponse save(@RequestBody TontineRequest tontineRequest){
        return tontineService.save(tontineRequest);
    }
    @PatchMapping("{id}")
    public TontineResponse update (@RequestBody TontineRequest tontineRequest,@PathVariable("id") Integer id){
        return tontineService.update(tontineRequest,id);
    }
    @DeleteMapping("/{id}")
    public void delete (Integer id){
         tontineService.delete(id);
    }
    @GetMapping("/adhesion")
    public List<AdhesionResponse> getAttenteAdhesion(@RequestParam Integer idTontine) {
        return adhesionService.getAdhesionAttente(idTontine);
    }

    @PostMapping("/adhesion")
    public AdhesionResponse save(@RequestBody AdhesionRequest adhesionRequest){
        return adhesionService.save(adhesionRequest);
    }
    @PatchMapping ("/adhesion")
    public AdhesionResponse traiterAdhesion(@RequestParam Integer idUser, @RequestParam Integer idTontine, @RequestBody UpdateStatusDto nouveau){
        return adhesionService.traiterAdhesion(idUser,nouveau,idTontine);
    }

}
