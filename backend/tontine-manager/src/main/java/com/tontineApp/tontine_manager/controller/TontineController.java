package com.tontineApp.tontine_manager.controller;

import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tontines")
@RequiredArgsConstructor
public class TontineController {

    private final TontineRepository tontineRepository;

    @GetMapping
    public ResponseEntity<List<Tontine>> getAllTontines() {
        List<Tontine> tontines = tontineRepository.findAll();
        return ResponseEntity.ok(tontines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tontine> getTontineById(@PathVariable Integer id) {
        return tontineRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tontine> createTontine(@RequestBody Tontine tontine) {
        Tontine saved = tontineRepository.save(tontine);
        return ResponseEntity.ok(saved);
    }
}
