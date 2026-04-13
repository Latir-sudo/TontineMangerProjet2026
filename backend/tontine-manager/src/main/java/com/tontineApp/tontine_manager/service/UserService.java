package com.tontineApp.tontine_manager.service;


import com.tontineApp.tontine_manager.dto.UserResponse;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.UserMapping;
import com.tontineApp.tontine_manager.model.User;
import com.tontineApp.tontine_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    // récupération utilisateurs

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findByNom(String nom) {
        return userRepository.findByNom(nom);
    }
    public List<User> findByPrenom(String prenom) {
        return userRepository.findByPrenom(prenom);
    }

    public User getById(Integer id) {
        return userRepository.findById(id).
                orElseThrow(()-> new RessourceNotFoundException("User not found"));
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    // ajout d'utilisateur

    public UserResponse saveUser(User user){
        return UserMapping.mapToUserResponse(userRepository.save(user));
    }

}
