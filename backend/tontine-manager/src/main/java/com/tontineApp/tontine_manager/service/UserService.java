package com.tontineApp.tontine_manager.service;


import com.tontineApp.tontine_manager.dto.UserRequest;
import com.tontineApp.tontine_manager.dto.UserResponse;
import com.tontineApp.tontine_manager.exception.RessourceNotFoundException;
import com.tontineApp.tontine_manager.mapper.UserMapping;
import com.tontineApp.tontine_manager.model.Role;
import com.tontineApp.tontine_manager.model.User;
import com.tontineApp.tontine_manager.repository.RoleRepository;
import com.tontineApp.tontine_manager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    // récupération utilisateurs

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserResponse> findByNom(String nom) {
        return userRepository.findByNom(nom).stream()
                .map(UserMapping::mapToUserResponse)
                .toList();
    }
    public List<UserResponse> findByPrenom(String prenom) {
        return userRepository.findByPrenom(prenom).stream()
                .map(UserMapping::mapToUserResponse)
                .toList();
    }

    public UserResponse getById(Integer id) {
        return UserMapping.mapToUserResponse(userRepository.findById(id).orElseThrow(()-> new RessourceNotFoundException("User not found")));

    }
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream()
                .map(UserMapping::mapToUserResponse)
                .toList();
    }

    // ajout d'utilisateur

    @Transactional
    public UserResponse saveUser(UserRequest userRequest, List<String> rolenames) {

        if(rolenames==null||rolenames.isEmpty()){
            throw new IllegalArgumentException("le role doit etre défini");
        }
        User user = UserMapping.mapUserRequestToUser(userRequest);

        for (String rolename : rolenames) {
            Role role = roleRepository.findByNomRole(rolename).
                    orElseGet(() -> {
                        Role newrole = new Role();
                        newrole.setNomRole(rolename);
                        return roleRepository.save(newrole);
                    });

            user.addRole(role);
        }
        return UserMapping.mapToUserResponse(userRepository.save(user));
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponse updateUser(UserRequest newuser,Integer id){

        if(newuser==null){
            throw new IllegalArgumentException("objet vide");

        }
        User user= userRepository.findById(id).orElseThrow(()-> new RessourceNotFoundException("User not found"));
        if(newuser.getNom()!=null && !newuser.getNom().isBlank())
            user.setNom(newuser.getNom());
        if(newuser.getPrenom()!=null && !newuser.getPrenom().isBlank())
            user.setPrenom(newuser.getPrenom());
        if(newuser.getUserPassword()!=null && !newuser.getUserPassword().isBlank())
            user.setUserPassword(newuser.getUserPassword());
        if(newuser.getEmail()!=null && !newuser.getEmail().isBlank())
            user.setEmail(newuser.getEmail());
        if(newuser.getTelephone()!=null  && !newuser.getTelephone().isBlank())
            user.setTelephone(newuser.getTelephone());

        return UserMapping.mapToUserResponse(userRepository.save(user));

    }

}
