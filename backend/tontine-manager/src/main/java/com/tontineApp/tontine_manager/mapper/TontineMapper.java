package com.tontineApp.tontine_manager.mapper;

import com.tontineApp.tontine_manager.dto.TontineRequest;
import com.tontineApp.tontine_manager.dto.TontineResponse;
import com.tontineApp.tontine_manager.model.Tontine;
import org.springframework.stereotype.Component;

@Component
public class TontineMapper {
    public TontineRequest toTontineRequest(Tontine tontine){
        TontineRequest response = new TontineRequest();
        response.setNomTontine(tontine.getNomTontine());
        response.setDescriptionTontine(tontine.getDescriptionTontine());
        response.setPolitiqueTontine(tontine.getPolitiqueTontine());
        response.setMontant(tontine.getMontant());
        response.setFrequence(tontine.getFrequence());
        response.setDateCreation(tontine.getDateCreation());

        return response;
    }

    public Tontine toTontine(TontineRequest tontineRequest){
        Tontine tontine = new  Tontine();
        tontine.setDescriptionTontine(tontineRequest.getDescriptionTontine());
        tontine.setNomTontine(tontineRequest.getNomTontine());
        tontine.setPolitiqueTontine(tontineRequest.getPolitiqueTontine());
        tontine.setFrequence(tontineRequest.getFrequence());
        tontine.setDateCreation(tontineRequest.getDateCreation());
        tontine.setNomTontine(tontineRequest.getNomTontine());
        return tontine;
    }

    public TontineResponse toTontineResponse(Tontine tontine) {
        TontineResponse tontineResponse = new TontineResponse();
        tontineResponse.setId(tontine.getId());
        tontineResponse.setFrequence(tontine.getFrequence());
        tontineResponse.setMontant(tontine.getMontant());
        tontineResponse.setNomTontine(tontine.getNomTontine());
        return tontineResponse;
    }
}
