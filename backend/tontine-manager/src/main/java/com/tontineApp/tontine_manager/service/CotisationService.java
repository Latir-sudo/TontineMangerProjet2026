package com.tontineApp.tontine_manager.service;


import com.tontineApp.tontine_manager.dto.CotisationRequest;
import com.tontineApp.tontine_manager.dto.CotisationResponse;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.CotisationMapper;
import com.tontineApp.tontine_manager.model.Cotisation;
import com.tontineApp.tontine_manager.model.Membre;
import com.tontineApp.tontine_manager.repository.CotisationRepository;
import com.tontineApp.tontine_manager.repository.MembreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tontineApp.tontine_manager.enumeration.StatutCotisation.COMPLET;
import static com.tontineApp.tontine_manager.enumeration.StatutCotisation.PARTIEL;

@Service
@AllArgsConstructor
public class CotisationService {

    private final CotisationRepository cotisationRepository;
    private final MembreRepository membreRepository;
    private final CotisationMapper cotisationMapper;

    public List<CotisationResponse> getCotisations(Integer idUser,Integer idTontine){
        // récupération du membre de la tontine
        Membre membre = membreRepository.findByTontine_IdAndUser_Id(idTontine,idUser).orElseThrow(()->new RessourceNotFoundException("membre de tontine"+idTontine +" et user "+idUser+" not found"));

        return cotisationRepository.findByMembre_Id(membre.getId()).stream()
                .map(cotisationMapper::toCotisationResponse)
                .toList();
    }

    public CotisationResponse getCotisationById(Integer id){
        return cotisationMapper.toCotisationResponse(cotisationRepository.findById(id).orElseThrow(()->new RessourceNotFoundException("cotisation "+id+" not found!")));
    }

    public void delete (Integer id){
        cotisationRepository.deleteById(id);
    }

    public CotisationResponse createCotisation(CotisationRequest cotisationRequest,Integer userid,Integer idtontine){

        Cotisation cotisation = cotisationMapper.toCotisation(cotisationRequest);
        Membre membre = membreRepository.findByTontine_IdAndUser_Id(idtontine,userid).orElseThrow(()->new RessourceNotFoundException("membre "+userid+" not found!"));
        cotisation.setMembre(membre);

        // vérification s'il s'agit d'une cotisation partielle ou totale
        if(membre.getTontine().getMontant()== cotisationRequest.getMontant()){
            cotisation.setStatut(COMPLET);
        }
        else {
            cotisation.setStatut(PARTIEL);
        }
        return  cotisationMapper.toCotisationResponse(cotisationRepository.save(cotisation));
    }
}
