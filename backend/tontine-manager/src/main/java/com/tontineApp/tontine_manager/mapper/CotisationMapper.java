package com.tontineApp.tontine_manager.mapper;

import com.tontineApp.tontine_manager.dto.CotisationRequest;
import com.tontineApp.tontine_manager.dto.CotisationResponse;
import com.tontineApp.tontine_manager.model.Cotisation;
import org.springframework.stereotype.Component;

import static com.tontineApp.tontine_manager.enumeration.StatutCotisation.PARTIEL;

@Component
public class CotisationMapper {

    public Cotisation toCotisation(CotisationRequest cotisationRequest) {
        Cotisation cotisation = new Cotisation();
        cotisation.setMontant(cotisationRequest.getMontant());
        // on renseignera le statut dans la couche service

        return cotisation;
    }
    public CotisationResponse toCotisationResponse(Cotisation cotisation) {
        CotisationResponse cotisationResponse = new CotisationResponse();
        cotisationResponse.setMontant(cotisation.getMontant());
        cotisationResponse.setStatutCotisation(cotisation.getStatut());
        cotisationResponse.setIdMembre(cotisation.getMembre().getId());

        return cotisationResponse;
    }
}
