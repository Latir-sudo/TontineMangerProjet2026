package com.tontineApp.tontine_manager.mapper;

import com.tontineApp.tontine_manager.dto.UserRequest;
import com.tontineApp.tontine_manager.dto.UserResponse;
import com.tontineApp.tontine_manager.model.Role;
import com.tontineApp.tontine_manager.model.User;
import org.springframework.stereotype.Component;


public class UserMapping {

    public static UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setNom(user.getNom());
        userResponse.setPrenom(user.getPrenom());
        userResponse.setEmail(user.getEmail());
        userResponse.setTelephone(user.getTelephone());
        userResponse.setDateInscription(user.getDateInscription());

        userResponse.setRoles(
                user.getRoles().stream()
                        .map(Role::getNomRole)
                        .toList()
        );

        return userResponse;
    }
    public static User mapUserRequestToUser(UserRequest userRequest) {
        User user = new User();
        user.setNom(userRequest.getNom());
        user.setPrenom(userRequest.getPrenom());
        user.setEmail(userRequest.getEmail());
        user.setDateInscription(userRequest.getDateInscription());
        user.setUserPassword(userRequest.getUserPassword());
        user.setVille(userRequest.getVille());
        user.setTelephone(userRequest.getTelephone());
        return user;
    }
}
