package com.tontineApp.tontine_manager.repository;

import com.tontineApp.tontine_manager.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByMembreIdOrderByDateCreationDesc(Integer membreId);

    List<Notification> findByMembreIdAndEstLuFalseOrderByDateCreationDesc(Integer membreId);

    List<Notification> findByMembreIdAndEstLuFalse(Integer membreId);

    Long countByMembreIdAndEstLuFalse(Integer membreId);

    List<Notification> findByTontineIdOrderByDateCreationDesc(Integer tontineId);

    void deleteByMembreId(Integer membreId);
}