package com.tontineApp.tontine_manager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/user")
    public String getUser(){
        return "Bonjour , user!";
    }

    @GetMapping("/admin")
    public String getAdmin(){
        return "Bonjour , admin!";
    }
}
