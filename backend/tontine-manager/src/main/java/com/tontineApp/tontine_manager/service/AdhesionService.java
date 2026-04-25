package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.AdhesionResponse;
import com.tontineApp.tontine_manager.dto.MembreRequest;
import com.tontineApp.tontine_manager.dto.UpdateStatusDto;
import com.tontineApp.tontine_manager.enumeration.StatutAdhesion;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.AdhesionMapper;
import com.tontineApp.tontine_manager.mapper.MembreMapper;
import com.tontineApp.tontine_manager.model.Adhesion;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.model.User;
import com.tontineApp.tontine_manager.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tontineApp.tontine_manager.enumeration.StatutAdhesion.ACCEPTEE;
import static com.tontineApp.tontine_manager.enumeration.StatutAdhesion.ATTENTE;

@Service
@AllArgsConstructor
public class AdhesionService {

    private final AdhesionRepository adhesionRepository;
    private final TontineRepository tontineRepository;
    private final MembreMapper membreMapper;
    private final MembreRepository membreRepository;
    public List<AdhesionResponse> getAdhesionAttente(Integer idTontine){
        if(!tontineRepository.existsById(idTontine)){
            throw new RessourceNotFoundException("Tontine non trouvé");
        }
        return adhesionRepository.findAllByStatutAndTontine_Id(ATTENTE,idTontine).stream().
                map(AdhesionMapper::toAdhesionResponse)
                .toList();
    }
    @Transactional
    public AdhesionResponse traiterAdhesion(Integer idUser , UpdateStatusDto nouveau, Integer idTontine){
        Adhesion adhesion= adhesionRepository.findByUser_idAndTontine_Id(idUser,idTontine)
                .orElseThrow(()->new RessourceNotFoundException("membre "+ idUser + "non trouve dans la tontine"+idTontine));

        if(nouveau==null || nouveau.getStatut()==null){
            throw new IllegalArgumentException("statut null");
        }
        adhesion.setStatut(nouveau.getStatut());
        adhesion.setDateAdhesion(nouveau.getDate());

        System.out.println("traiterAdhesion");

        if(ACCEPTEE.equals(nouveau.getStatut())){
            MembreRequest membreRequest = new MembreRequest();
            membreRequest.setIdTontine(idTontine);
            membreRequest.setIdUser(idUser);
            membreRepository.save(membreMapper.toMembre(membreRequest));
        }

        return AdhesionMapper.toAdhesionResponse(adhesionRepository.save(adhesion));

    }
}
