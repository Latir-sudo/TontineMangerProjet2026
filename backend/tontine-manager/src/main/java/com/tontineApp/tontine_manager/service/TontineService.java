package com.tontineApp.tontine_manager.service;


import com.tontineApp.tontine_manager.dto.MembreRequest;
import com.tontineApp.tontine_manager.dto.TontineRequest;
import com.tontineApp.tontine_manager.dto.TontineResponse;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.TontineMapper;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.model.Users;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import com.tontineApp.tontine_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TontineService{
    private final TontineRepository tontineRepository;
    private final TontineMapper tontineMapper;
    private final UserRepository userRepository;
    private final MembreService membreService;

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

    @Transactional(readOnly = true)
    public List<TontineResponse> getTontinesByMemberEmail(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Récupérer les tontines où l'utilisateur est membre
        return tontineRepository.findTontinesByUserId(user.getId()).stream()
                .map(tontineMapper::toTontineResponse)
                .collect(Collectors.toList());
    }
   public List<TontineResponse> getByCategorie(String categorie){
       return tontineRepository.findByCategorieTontine(categorie).stream()
               .map(tontineMapper::toTontineResponse)
               .toList();
   }
   public TontineResponse getById(Integer id){
       return tontineMapper.toTontineResponse(tontineRepository.findById(id).orElseThrow(()->new RessourceNotFoundException("tontine non trouvée")));
   }

    @Transactional (readOnly = true)
    public List<TontineResponse> getTontinesByUserId(Integer userId) {
        return tontineRepository.findTontinesByUserId(userId).stream()
                .map(tontineMapper::toTontineResponse)
                .collect(Collectors.toList());
    }

    // Alternative avec email
    @Transactional(readOnly = true)
    public List<TontineResponse> getMesTontines(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return getTontinesByUserId(user.getId());
    }

    @Transactional
    public TontineResponse save(TontineRequest tontineRequest, String adminEmail) {
        Users admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé"));

        Tontine tontine = tontineMapper.toTontine(tontineRequest);
        tontine.setAdmin(admin);  // ← L'utilisateur connecté devient admin

        // l'admin devient membree de la tontine qu'il crée

        Users user= userRepository.findByEmail(adminEmail).orElseThrow(()->new RessourceNotFoundException("utilisateur avec l'email"+adminEmail));
        MembreRequest membreRequest=new MembreRequest(user.getId(),tontine.getId());
        membreService.ajouterUtilisateurATontine(membreRequest);

        tontine.setDateCreation(LocalDate.now());
        tontine.setStatutTontine("active");
        tontine.setNombreMembres(0);

        return tontineMapper.toTontineResponse(tontineRepository.save(tontine));
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