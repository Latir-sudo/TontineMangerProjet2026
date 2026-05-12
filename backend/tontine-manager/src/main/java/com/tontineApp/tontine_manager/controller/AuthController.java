package com.tontineApp.tontine_manager.controller;

import com.tontineApp.tontine_manager.dto.AuthResponse;
import com.tontineApp.tontine_manager.dto.LoginRequest;
import com.tontineApp.tontine_manager.dto.UserRequest;
import com.tontineApp.tontine_manager.dto.UserResponse;
import com.tontineApp.tontine_manager.service.JwtService;
import com.tontineApp.tontine_manager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;  // ← Ajouter
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            //

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());



           // String token = jwtService.generateToken(authentication);

            String token = jwtService.generateToken(userDetails);

            // Récupérer l'utilisateur complet
            UserResponse user = userService.getUserByEmail(loginRequest.getEmail());

            AuthResponse response = new AuthResponse(
                    true,
                    "Connexion réussie",
                    token,
                    user
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            AuthResponse errorResponse = new AuthResponse(
                    false,
                    "Email ou mot de passe incorrect",
                    null,
                    null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRequest userRequest) {
        try {
            // Vérifier si l'email existe déjà
            if (userService.existsByEmail(userRequest.getEmail())) {
                AuthResponse errorResponse = new AuthResponse(
                        false,
                        "Cet email est déjà utilisé",
                        null,
                        null
                );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }

            // Créer l'utilisateur
            UserResponse newUser = userService.saveUser(userRequest);

            // Générer le token
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword())
            );
            String token = jwtService.generateToken(authentication);

            AuthResponse response = new AuthResponse(
                    true,
                    "Inscription réussie",
                    token,
                    newUser
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            AuthResponse errorResponse = new AuthResponse(
                    false,
                    "Erreur lors de l'inscription: " + e.getMessage(),
                    null,
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}