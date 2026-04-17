package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.AdhesionResponse;
import com.tontineApp.tontine_manager.dto.UpdateStatusDto;
import com.tontineApp.tontine_manager.enumeration.StatutAdhesion;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.AdhesionMapper;
import com.tontineApp.tontine_manager.model.Adhesion;
import com.tontineApp.tontine_manager.repository.AdhesionRepository;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdhesionService {

    private final AdhesionRepository adhesionRepository;
    private final TontineRepository tontineRepository;
    public AdhesionService(AdhesionRepository adhesionRepository, TontineRepository tontineRepository) {
        this.adhesionRepository = adhesionRepository;
        this.tontineRepository = tontineRepository;
    }

    public List<AdhesionResponse> getAdhesionAttente(Integer idTontine){
        if(!tontineRepository.existsById(idTontine)){
            throw new RessourceNotFoundException("Tontine non trouvé");
        }
        return adhesionRepository.findAllByStatutAndTontine_Id("attente",idTontine).stream().
                map(AdhesionMapper::toAdhesionResponse)
                .toList();
    }

    public AdhesionResponse traiterAdhesion(Integer idMembre , UpdateStatusDto nouveau, Integer idTontine){
        Adhesion adhesion= adhesionRepository.findByUser_idAndTontine_Id(idMembre,idTontine)
                .orElseThrow(()->new RessourceNotFoundException("membre "+ idMembre + "non trouve dans la tontine"+idTontine));

        adhesion.setStatut(nouveau.getStatut());
        adhesion.setDateAdhesion(nouveau.getDate());



        return AdhesionMapper.toAdhesionResponse(adhesionRepository.save(adhesion));

    }
}
