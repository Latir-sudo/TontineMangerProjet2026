package com.tontineApp.tontine_manager.controller;


import com.tontineApp.tontine_manager.dto.MembreRequest;
import com.tontineApp.tontine_manager.service.MembreService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tontine/{idtontine}/membre")
public class MembreController {


    private final MembreService membreService;
    public MembreController(MembreService membreService){
        this.membreService=membreService;
    }
    @PostMapping("/{id}")
    public MembreRequest ajoutMembre (@RequestBody MembreRequest membreRequest) {
        return membreService.save(membreRequest);
    }
}
