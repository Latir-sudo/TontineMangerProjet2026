package com.tontineApp.tontine_manager.controller;

import com.tontineApp.tontine_manager.dto.TontineRequest;
import com.tontineApp.tontine_manager.service.TontineService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tontine")
public class TontineController {

    private final TontineService tontineService;
    TontineController(TontineService tontineService) {
        this.tontineService = tontineService;
    }

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
}
