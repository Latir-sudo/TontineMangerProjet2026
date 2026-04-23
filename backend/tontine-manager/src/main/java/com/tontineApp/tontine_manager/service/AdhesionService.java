package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.AdhesionResponse;
import com.tontineApp.tontine_manager.dto.MembreRequest;
import com.tontineApp.tontine_manager.dto.UpdateStatusDto;
import com.tontineApp.tontine_manager.enumeration.StatutAdhesion;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.AdhesionMapper;
import com.tontineApp.tontine_manager.model.Adhesion;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.model.User;
import com.tontineApp.tontine_manager.repository.AdhesionRepository;
import com.tontineApp.tontine_manager.repository.TontineMembreRepository;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import com.tontineApp.tontine_manager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tontineApp.tontine_manager.enumeration.StatutAdhesion.ACCEPTEE;

@Service
public class AdhesionService {

    private final AdhesionRepository adhesionRepository;
    private final TontineRepository tontineRepository;
    private final TontineMembreRepository tontineMembreRepository;
    private final UserRepository userRepository;
    private final MembreService membreService;
    public AdhesionService(AdhesionRepository adhesionRepository, TontineRepository tontineRepository,TontineMembreRepository tm,UserRepository userRepository,MembreService membreService ) {
        this.adhesionRepository = adhesionRepository;
        this.tontineRepository = tontineRepository;
        this.tontineMembreRepository=tm;
        this.userRepository = userRepository;
        this.membreService = membreService;
    }

    public List<AdhesionResponse> getAdhesionAttente(Integer idTontine){
        if(!tontineRepository.existsById(idTontine)){
            throw new RessourceNotFoundException("Tontine non trouvé");
        }
        return adhesionRepository.findAllByStatutAndTontine_Id("attente",idTontine).stream().
                map(AdhesionMapper::toAdhesionResponse)
                .toList();
    }

    @Transactional
    public AdhesionResponse traiterAdhesion(Integer idUser , UpdateStatusDto nouveau, Integer idTontine){
        Adhesion adhesion= adhesionRepository.findByUser_idAndTontine_Id(idUser,idTontine)
                .orElseThrow(()->new RessourceNotFoundException("membre "+ idUser + "non trouve dans la tontine"+idTontine));

        adhesion.setStatut(nouveau.getStatut());
        adhesion.setDateAdhesion(nouveau.getDate());

        if(nouveau.getStatut()==ACCEPTEE){
            MembreRequest membreRequest = new MembreRequest();
            membreRequest.setIdTontine(idTontine);
            membreRequest.setDateAdhesion(nouveau.getDate());
            membreRequest.setIdUser(idUser);
            membreService.save(membreRequest);
        }

        return AdhesionMapper.toAdhesionResponse(adhesionRepository.save(adhesion));

    }
}
