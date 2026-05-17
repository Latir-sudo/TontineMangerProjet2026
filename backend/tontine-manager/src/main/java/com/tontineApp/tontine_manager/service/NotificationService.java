package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.NotificationDTO;
import com.tontineApp.tontine_manager.enumeration.StatutNotification;
import com.tontineApp.tontine_manager.model.Membre;
import com.tontineApp.tontine_manager.model.Notification;
import com.tontineApp.tontine_manager.model.Tontine;
import com.tontineApp.tontine_manager.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // Créer une notification
    @Transactional
    public Notification createNotification(String titre, String message, String couleur,
                                           Membre membre, Tontine tontine, String typeNotification) {
        Notification notification = Notification.builder()
                .titre(titre)
                .message(message)
                .heureRelative("à l'instant")
                .couleur(couleur)
                .estLu(false)
                .dateCreation(LocalDateTime.now())
                .membre(membre)
                .tontine(tontine)
                .statutNotification(StatutNotification.NON_LU)
                .typeNotification(typeNotification)
                .build();

        return notificationRepository.save(notification);
    }

    // Créer une notification de paiement validé
    public Notification creerNotificationPaiementValide(Membre membre, Tontine tontine, Integer montant) {
        String titre = "Paiement validé";
        String message = String.format("Votre cotisation de %d FCFA a été validée", montant);
        String couleur = "#5bbf7e";

        return createNotification(titre, message, couleur, membre, tontine, "PAIEMENT");
    }

    // Créer un rappel de cotisation
    public Notification creerRappelCotisation(Membre membre, Tontine tontine, Integer joursRestants) {
        String titre = "Rappel de cotisation";
        String message = String.format("Votre cotisation arrive à échéance dans %d jours", joursRestants);
        String couleur = "#ffc86a";

        return createNotification(titre, message, couleur, membre, tontine, "RAPPEL");
    }

    // Créer une notification nouveau membre
    public Notification creerNotificationNouveauMembre(Membre nouveauMembre, Tontine tontine) {
        String titre = "Nouveau membre";
        String message = String.format("%s a rejoint votre tontine", nouveauMembre.getNom());
        String couleur = "#97b3ff";

        return createNotification(titre, message, couleur, null, tontine, "MEMBRE");
    }

    // Créer une notification cotisation en retard
    public Notification creerNotificationRetard(Membre membre, Tontine tontine, String mois) {
        String titre = "Cotisation en retard";
        String message = String.format("Votre cotisation de %s est en retard", mois);
        String couleur = "#ff7262";

        return createNotification(titre, message, couleur, membre, tontine, "RETARD");
    }

    // Récupérer toutes les notifications d'un membre
    public List<NotificationDTO> getNotificationsByMembre(Integer membreId) {
        List<Notification> notifications = notificationRepository.findByMembreIdOrderByDateCreationDesc(membreId);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Récupérer les notifications non lues d'un membre
    public List<NotificationDTO> getNotificationsNonLues(Integer membreId) {
        List<Notification> notifications = notificationRepository.findByMembreIdAndEstLuFalseOrderByDateCreationDesc(membreId);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Marquer une notification comme lue
    @Transactional
    public void marquerCommeLue(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        notification.marquerCommeLu();
        notificationRepository.save(notification);
    }

    // Tout marquer comme lu pour un membre
    @Transactional
    public void toutMarquerCommeLu(Integer membreId) {
        List<Notification> notifications = notificationRepository.findByMembreIdAndEstLuFalse(membreId);
        notifications.forEach(Notification::marquerCommeLu);
        notificationRepository.saveAll(notifications);
    }

    // Supprimer une notification
    @Transactional
    public void supprimerNotification(Integer notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    // Compter les notifications non lues
    public Long compterNotificationsNonLues(Integer membreId) {
        return notificationRepository.countByMembreIdAndEstLuFalse(membreId);
    }

    // Convertir en DTO
    private NotificationDTO convertToDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .titre(notification.getTitre())
                .message(notification.getMessage())
                .tempsRelatif(notification.getTempsRelatif())
                .couleur(notification.getCouleur())
                .estLu(notification.getEstLu())
                .dateCreation(notification.getDateFormatee())
                .typeNotification(notification.getTypeNotification())
                .lienAction(notification.getLienAction())
                .build();
    }

    // Charger les notifications par défaut pour un nouveau membre
    @Transactional
    public void chargerNotificationsParDefaut(Membre membre, Tontine tontine) {
        // Créer des exemples de notifications pour le nouveau membre
        creerNotificationPaiementValide(membre, tontine, 5000);
        creerRappelCotisation(membre, tontine, 3);
        creerNotificationRetard(membre, tontine, "Mai");

        // Notification pour un nouveau membre (simulée)
        Membre nouveauMembre = new Membre();
        nouveauMembre.setNom("Khadidja Sow");
        creerNotificationNouveauMembre(nouveauMembre, tontine);
    }
}