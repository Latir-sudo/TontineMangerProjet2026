package com.tontineApp.tontine_manager.mapper;


import com.tontineApp.tontine_manager.dto.AdhesionRequest;
import com.tontineApp.tontine_manager.dto.AdhesionResponse;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.model.Adhesion;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.model.User;
import com.tontineApp.tontine_manager.repository.TontineRepository;
import com.tontineApp.tontine_manager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.tontineApp.tontine_manager.enumeration.StatutAdhesion.ATTENTE;

@Component
@AllArgsConstructor
public class AdhesionMapper {

    private final UserRepository userRepository;
    private final TontineRepository tontineRepository;

    public AdhesionResponse toAdhesionResponse(Adhesion adhesion) {
        AdhesionResponse adhesionResponse = new AdhesionResponse();
        adhesionResponse.setDateAdhesion(adhesion.getDateAdhesion());
        adhesionResponse.setPrenomUser(adhesion.getUser().getPrenom());
        adhesionResponse.setNomUser(adhesion.getUser().getNom());
        adhesionResponse.setTelephoneUser(adhesion.getUser().getTelephone());
        adhesionResponse.setStatut(adhesion.getStatut());

        return adhesionResponse;
    }

    public AdhesionRequest toAdhesionRequest(Adhesion adhesion){
        AdhesionRequest adhesionRequest = new AdhesionRequest();
        adhesionRequest.setDateAdhesion(adhesion.getDateAdhesion());
        adhesionRequest.setIdTontine(adhesion.getUser().getId());
        adhesionRequest.setIdTontine(adhesion.getTontine().getId());

        return adhesionRequest;
    }

    public Adhesion toAdhesion(AdhesionRequest adhesionRequest){
        Adhesion adhesion = new Adhesion();
        User user = userRepository.findById(adhesionRequest.getIdUser()).orElseThrow(()->new RessourceNotFoundException("user not found"));
        Tontine tontine = tontineRepository.findById(adhesionRequest.getIdTontine()).orElseThrow(()->new RessourceNotFoundException("tontine not found"));
       adhesion.setUser(user);
       adhesion.setTontine(tontine);
       adhesion.setDateAdhesion(adhesionRequest.getDateAdhesion());
       adhesion.setStatut(ATTENTE);

       return adhesion;
    }


}
