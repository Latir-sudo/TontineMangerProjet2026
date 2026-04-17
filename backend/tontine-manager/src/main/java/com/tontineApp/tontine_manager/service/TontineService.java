package com.tontineApp.tontine_manager.service;


import com.tontineApp.tontine_manager.dto.TontineRequest;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.TontineMapper;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TontineService {

    private final TontineRepository tontineRepository;
    public TontineService(TontineRepository tontineRepository) {
        this.tontineRepository = tontineRepository;
    }

    public List<TontineRequest> getAllTontine(){
        return tontineRepository.findAll().stream()
                .map(TontineMapper::toTontineRequest)
                .toList();
    }

    public List<TontineRequest> getTontineByNom(String nomTontine){
        return tontineRepository.findByNomTontine(nomTontine).stream()
                .map(TontineMapper::toTontineRequest)
                .toList();
    }
    public List<TontineRequest> getTontineByMontant(Integer montant){
        return tontineRepository.findByMontant(montant).stream()
                .map(TontineMapper::toTontineRequest)
                .toList();
    }

    public List<TontineRequest> getTontineByFrequence(String frequence){
        return tontineRepository.findByFrequence(frequence).stream()
                .map(TontineMapper::toTontineRequest)
                .toList();
    }

    public TontineRequest getTontineById(Integer id){
        return TontineMapper.toTontineRequest(tontineRepository.findById(id).
                orElseThrow(()-> new RessourceNotFoundException("tontine non trouvé")));
    }

    @Transactional
    public TontineRequest save(TontineRequest tontineRequest){
       Tontine tontine = TontineMapper.toTontine(tontineRequest);
       tontineRepository.save(tontine);
       return tontineRequest;
    }

    public void delete (Integer id){
        tontineRepository.deleteById(id);
    }


    public TontineRequest update(TontineRequest tontineRequest,Integer id){



    }

}
