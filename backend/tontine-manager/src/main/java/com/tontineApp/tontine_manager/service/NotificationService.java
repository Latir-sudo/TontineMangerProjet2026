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

    @Transactional
    public Notification createNotification(String titre, String message, String couleur,
                                           Membre membre, Tontine tontine, String typeNotification) {
        Notification notification = Notification.builder()
                .titre(titre)
                .message(message)
                .heureRelative("a l'instant")
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

    public Notification creerNotificationPaiementValide(Membre membre, Tontine tontine, Integer montant) {
        String titre = "Paiement valide";
        String message = String.format("Votre cotisation de %d FCFA a ete validee", montant);
        String couleur = "#5bbf7e";

        return createNotification(titre, message, couleur, membre, tontine, "PAIEMENT");
    }

    public Notification creerRappelCotisation(Membre membre, Tontine tontine, Integer joursRestants) {
        String titre = "Rappel de cotisation";
        String message = String.format("Votre cotisation arrive a echeance dans %d jours", joursRestants);
        String couleur = "#ffc86a";

        return createNotification(titre, message, couleur, membre, tontine, "RAPPEL");
    }

    public Notification creerNotificationNouveauMembre(Membre nouveauMembre, Tontine tontine) {
        String titre = "Nouveau membre";
        String message = String.format("%s a rejoint votre tontine", getNomComplet(nouveauMembre));
        String couleur = "#97b3ff";

        return createNotification(titre, message, couleur, null, tontine, "MEMBRE");
    }

    public Notification creerNotificationRetard(Membre membre, Tontine tontine, String mois) {
        String titre = "Cotisation en retard";
        String message = String.format("Votre cotisation de %s est en retard", mois);
        String couleur = "#ff7262";

        return createNotification(titre, message, couleur, membre, tontine, "RETARD");
    }

    public List<NotificationDTO> getNotificationsByMembre(Integer membreId) {
        List<Notification> notifications = notificationRepository.findByMembreIdOrderByDateCreationDesc(membreId);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationsNonLues(Integer membreId) {
        List<Notification> notifications = notificationRepository.findByMembreIdAndEstLuFalseOrderByDateCreationDesc(membreId);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void marquerCommeLue(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvee"));
        notification.marquerCommeLu();
        notificationRepository.save(notification);
    }

    @Transactional
    public void toutMarquerCommeLu(Integer membreId) {
        List<Notification> notifications = notificationRepository.findByMembreIdAndEstLuFalse(membreId);
        notifications.forEach(Notification::marquerCommeLu);
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void supprimerNotification(Integer notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public Long compterNotificationsNonLues(Integer membreId) {
        return notificationRepository.countByMembreIdAndEstLuFalse(membreId);
    }

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

    @Transactional
    public void chargerNotificationsParDefaut(Membre membre, Tontine tontine) {
        creerNotificationPaiementValide(membre, tontine, 5000);
        creerRappelCotisation(membre, tontine, 3);
        creerNotificationRetard(membre, tontine, "Mai");
        creerNotificationNouveauMembre(membre, tontine);
    }

    private String getNomComplet(Membre membre) {
        if (membre == null || membre.getUser() == null) {
            return "Un membre";
        }

        String prenom = membre.getUser().getPrenom() == null ? "" : membre.getUser().getPrenom();
        String nom = membre.getUser().getNom() == null ? "" : membre.getUser().getNom();
        String nomComplet = (prenom + " " + nom).trim();

        return nomComplet.isEmpty() ? "Un membre" : nomComplet;
    }
}
