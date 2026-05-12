package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.UserResponse;
import com.tontineApp.tontine_manager.model.Users;
import com.tontineApp.tontine_manager.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;

    // Clé secrète
    private final String SECRET_KEY = "QiAT1hs6emp0qZZqikMrGcJVl/h8cULpkyJfp/cZlgHhsBtlX3iaACqNMA7EnW55pNN4oqppetIcHdZw7hfVhA==";
    private final long EXPIRATION = 86400000; // 24h

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // Convertir User en Map (pour stocker dans JWT)
    private Map<String, Object> convertUserToMap(Users user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("nom", user.getNom());
        userMap.put("prenom", user.getPrenom());
        userMap.put("email", user.getEmail());
        userMap.put("telephone", user.getTelephone());
        userMap.put("ville", user.getVille());
        userMap.put("dateInscription", user.getDateInscription() != null ? user.getDateInscription().toString() : null);
        userMap.put("roles", user.getRoles().stream().map(role -> role.getNomRole()).toList());
        return userMap;
    }
    // Convertir UserResponse en Map
    private Map<String, Object> convertUserResponseToMap(UserResponse user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("nom", user.getNom());
        userMap.put("prenom", user.getPrenom());
        userMap.put("email", user.getEmail());
        userMap.put("telephone", user.getTelephone());
        userMap.put("ville", user.getVille());
        userMap.put("dateInscription", user.getDateInscription() != null ? user.getDateInscription().toString() : null);
        userMap.put("roles", user.getRoles());
        return userMap;
    }
    // Générer token à partir d'un Authentication
    public String generateToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();

        String email = authentication.getName();
        userRepository.findByEmail(email).ifPresent(user -> {
            // Stocker tout l'objet utilisateur dans le claim "user"
            claims.put("user", convertUserToMap(user));
        });

        return generateToken(claims, email);
    }
    // Générer token à partir d'un UserDetails
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        userRepository.findByEmail(userDetails.getUsername()).ifPresent(user -> {
            claims.put("user", convertUserToMap(user));
        });

        return generateToken(claims, userDetails.getUsername());
    }
    // Générer token à partir d'un UserResponse (pour inscription)
    public String generateToken(UserResponse user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", convertUserResponseToMap(user));
        return generateToken(claims, user.getEmail());
    }
    // Méthode privée pour générer le token
    private String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey())
                .compact();
    }
    // Vérifier si le token est valide
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    // Extraire le username (email) du token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    // Extraire l'expiration du token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    // Vérifier si le token est expiré
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    // Extraire un claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // Extraire tous les claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    // Extraire l'utilisateur complet du token (Map)
    @SuppressWarnings("unchecked")
    public Map<String, Object> extractUserFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("user", Map.class);
    }

    // Extraire l'utilisateur complet et le convertir en UserResponse
    public UserResponse extractUserResponseFromToken(String token) {
        Map<String, Object> userMap = extractUserFromToken(token);
        UserResponse user = new UserResponse();
        user.setId((Integer) userMap.get("id"));
        user.setNom((String) userMap.get("nom"));
        user.setPrenom((String) userMap.get("prenom"));
        user.setEmail((String) userMap.get("email"));
        user.setTelephone((String) userMap.get("telephone"));
        user.setVille((String) userMap.get("ville"));
        user.setRoles((List<String>) userMap.get("roles"));
        return user;
    }
}