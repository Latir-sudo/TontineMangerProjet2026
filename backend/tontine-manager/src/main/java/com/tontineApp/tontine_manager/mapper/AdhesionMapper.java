package com.tontineApp.tontine_manager.mapper;


import com.tontineApp.tontine_manager.dto.AdhesionResponse;
import com.tontineApp.tontine_manager.model.Adhesion;

public class AdhesionMapper {

    public static AdhesionResponse toAdhesionResponse(Adhesion adhesion) {
        AdhesionResponse adhesionResponse = new AdhesionResponse();
        adhesionResponse.setDateAdhesion(adhesion.getDateAdhesion());
        adhesionResponse.setPrenomUser(adhesion.getUser().getPrenom());
        adhesionResponse.setNomUser(adhesion.getUser().getNom());
        adhesionResponse.setTelephoneUser(adhesion.getUser().getTelephone());
        adhesionResponse.setStatut(adhesion.getStatut());

        return adhesionResponse;
    }
}
