package com.tontineApp.tontine_manager.service;


import com.tontineApp.tontine_manager.dto.TontineRequest;
import com.tontineApp.tontine_manager.dto.TontineResponse;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.TontineMapper;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class TontineService {

    private final TontineRepository tontineRepository;
    private final TontineMapper tontineMapper;

    public List<TontineRequest> getAllTontine(){
        return tontineRepository.findAll().stream()
                .map(tontineMapper::toTontineRequest)
                .toList();
    }

    public List<TontineResponse> getTontineByNom(String nomTontine){
        return tontineRepository.findByNomTontine(nomTontine).stream()
                .map(tontineMapper::toTontineResponse)
                .toList();
    }
    public List<TontineResponse> getTontineByMontant(Integer montant){
        return tontineRepository.findByMontant(montant).stream()
                .map(tontineMapper::toTontineResponse)
                .toList();
    }

    public List<TontineResponse> getTontineByFrequence(String frequence){
        return tontineRepository.findByFrequence(frequence).stream()
                .map(tontineMapper::toTontineResponse)
                .toList();
    }

    public TontineResponse getTontineById(Integer id){
        return tontineMapper.toTontineResponse(tontineRepository.findById(id).
                orElseThrow(()-> new RessourceNotFoundException("tontine non trouvé")));
    }

    @Transactional
    public TontineResponse save(TontineRequest tontineRequest){
       Tontine tontine = tontineMapper.toTontine(tontineRequest);
       return tontineMapper.toTontineResponse(tontineRepository.save(tontine));
    }

    public void delete (Integer id){
        tontineRepository.deleteById(id);
    }

    @Transactional
    public TontineResponse update(TontineRequest tontineRequest,Integer id){
        if(tontineRequest==null){
            throw new IllegalArgumentException("tontine null");
        }
        Tontine newtontine = tontineRepository.findById(id).orElseThrow(()->new RessourceNotFoundException("tontine pas trouve"));
        if(tontineRequest.getNomTontine() !=null && !tontineRequest.getNomTontine().isBlank()){
            newtontine.setNomTontine(tontineRequest.getNomTontine());
        }
        if(tontineRequest.getFrequence()!=null && !tontineRequest.getFrequence().isBlank()){
            newtontine.setFrequence(tontineRequest.getFrequence());
        }
        if(tontineRequest.getDescriptionTontine()!=null && !tontineRequest.getDescriptionTontine().isBlank()){
            newtontine.setDescriptionTontine(tontineRequest.getDescriptionTontine());
        }
        if (tontineRequest.getMontant()!=null){
            newtontine.setMontant(tontineRequest.getMontant());
        }
        if(tontineRequest.getPolitiqueTontine()!=null && !tontineRequest.getPolitiqueTontine().isBlank()){
            newtontine.setPolitiqueTontine(tontineRequest.getPolitiqueTontine());
        }

        return tontineMapper.toTontineResponse(tontineRepository.save(newtontine));
    }

}
