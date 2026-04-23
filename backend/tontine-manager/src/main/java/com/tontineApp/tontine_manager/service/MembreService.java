package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.MembreRequest;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.MembreMapper;
import com.tontineApp.tontine_manager.model.Membre;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.model.User;
import com.tontineApp.tontine_manager.repository.TontineMembreRepository;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import com.tontineApp.tontine_manager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembreService {

    private final TontineMembreRepository tontineMembreRepository;
    private final UserRepository userRepository;
    private final TontineRepository tontineRepository;

    private final MembreMapper membreMapper;
    public MembreService(TontineMembreRepository tontineMembreRepository,UserRepository userRepository,TontineRepository tontineRepository,MembreMapper membreMapper) {
        this.tontineMembreRepository = tontineMembreRepository;
        this.userRepository = userRepository;
        this.tontineRepository = tontineRepository;
        this.membreMapper = membreMapper;
    }
    public List<MembreRequest> findAll() {
        return tontineMembreRepository.findAll().stream()
                .map(membreMapper::toMembreRequest)
                .toList();
    }

    @Transactional
    public MembreRequest save(MembreRequest membreRequest) {
        if(membreRequest==null){
            throw new IllegalArgumentException("membreRequest is null");
        }

        User user = userRepository.findById(membreRequest.getIdUser()).orElseThrow(()->new RessourceNotFoundException("membre non trouve"));
        Tontine tontine = tontineRepository.findById(membreRequest.getIdTontine()).orElseThrow(()->new RessourceNotFoundException("tontine non trouve"));

        Membre membre = membreMapper.toMembre(membreRequest);
        membre.setUser(user);
        membre.setTontine(tontine);
        return membreMapper.toMembreRequest(tontineMembreRepository.save(membre));
    }
}
