package com.tontineApp.tontine_manager.mapper;

import com.tontineApp.tontine_manager.dto.UserResponse;
import com.tontineApp.tontine_manager.model.User;
import org.springframework.stereotype.Component;


public class UserMapping {

    public static UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setNom(user.getNom());
        userResponse.setPrenom(user.getPrenom());
        userResponse.setEmail(user.getEmail());
        userResponse.setDateInscription(user.getDateInscription());

        return userResponse;
    }
}
