package com.tontineApp.tontine_manager.controller;

import com.tontineApp.tontine_manager.dto.UserRequest;
import com.tontineApp.tontine_manager.dto.UserResponse;
import com.tontineApp.tontine_manager.model.User;
import com.tontineApp.tontine_manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/listusers")
    public List<UserResponse> getUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable("id") int id) {
        return userService.getById(id);
    }
    @PostMapping("/save")
    public UserResponse saveUser(@Valid @RequestBody UserRequest userRequest) {
        System.out.println("bonjour");
         return userService.saveUser(userRequest);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer id){
        userService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    public UserResponse updateUser(@PathVariable("id") int id, @Valid @RequestBody UserRequest userRequest) {
        return userService.updateUser(userRequest,id);
    }
}
