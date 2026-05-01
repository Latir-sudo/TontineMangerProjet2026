package com.tontineApp.tontine_manager.service;


import com.tontineApp.tontine_manager.dto.TontineRequest;
import com.tontineApp.tontine_manager.dto.TontineResponse;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.TontineMapper;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TontineService{
    private final TontineRepository tontineRepository;
    private final TontineMapper tontineMapper;

   public TontineService(TontineRepository tontineRepository,TontineMapper tontineMapper){
       this.tontineRepository=tontineRepository;
       this.tontineMapper=tontineMapper;
   }
   public List<TontineResponse> getAllTontines() {
       return tontineRepository.findAll().stream()
               .map(tontineMapper::toTontineResponse)
               .toList();
   }
   public List<TontineResponse> getParRegion(String region){
       return tontineRepository.findByRegionTontine(region).stream()
               .map(tontineMapper::toTontineResponse)
               .toList();
   }
   public List<TontineResponse> getByCategorie(String categorie){
       return tontineRepository.findByCategorieTontine(categorie).stream()
               .map(tontineMapper::toTontineResponse)
               .toList();
   }
   public TontineResponse getById(Integer id){
       return tontineMapper.toTontineResponse(tontineRepository.findById(id).orElseThrow(()->new RessourceNotFoundException("tontine non trouvée")));
   }

   public TontineResponse save (TontineRequest tontineRequest){
       return tontineMapper.toTontineResponse(tontineRepository.save(tontineMapper.toTontine(tontineRequest)));
   }

   public void delete (Integer id){
        tontineRepository.deleteById(id);
   }

   public TontineResponse update (TontineRequest tontineRequest,Integer id){
       Tontine tontine = tontineRepository.findById(id).orElseThrow(()->new RessourceNotFoundException("tontine non trouvé"));
       if(tontineRequest==null)
           throw new IllegalArgumentException("tontine null");
       if(tontineRequest.getDescriptionTontine()!=null && !tontineRequest.getDescriptionTontine().isBlank()){
           tontine.setDescriptionTontine(tontineRequest.getDescriptionTontine());
       }
       if(tontineRequest.getCategorie()!=null && !tontineRequest.getCategorie().isBlank()){
           tontine.setPolitiqueTontine(tontineRequest.getPolitiqueTontine());
       }
       if(tontineRequest.getNomTontine()!=null&& !tontineRequest.getNomTontine().isBlank()){
           tontine.setNomTontine(tontineRequest.getNomTontine());
       }
       if(tontineRequest.getFrequence()!=null && !tontineRequest.getFrequence().isBlank()){
           tontine.setFrequence(tontineRequest.getFrequence());
       }
       if(tontineRequest.getMontant()!=null){
           tontine.setMontant(tontineRequest.getMontant());
       }
       if(tontineRequest.getRegion()!=null && !tontineRequest.getRegion().isBlank()){
           tontine.setRegionTontine(tontineRequest.getRegion());
       }

       if(tontineRequest.getCategorie()!=null &&  !tontineRequest.getCategorie().isBlank()){
           tontine.setCategorieTontine(tontineRequest.getCategorie());
       }

       return tontineMapper.toTontineResponse(tontineRepository.save(tontine));

   }





}