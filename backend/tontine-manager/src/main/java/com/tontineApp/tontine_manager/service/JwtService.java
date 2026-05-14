package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.dto.UserTokenDto;
import com.tontineApp.tontine_manager.model.Users;
import com.tontineApp.tontine_manager.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;

    private final String jwtkey = "QiAT1hs6emp0qZZqikMrGcJVl/h8cULpkyJfp/cZlgHhsBtlX3iaACqNMA7EnW55pNN4oqppetIcHdZw7hfVhA==";
    private final long EXPIRATION = 86400000;

    private UserTokenDto convertToTokenDto(Users user) {
        UserTokenDto dto = new UserTokenDto();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setEmail(user.getEmail());
        dto.setTelephone(user.getTelephone());
        dto.setVille(user.getVille());
        dto.setDateInscription(user.getDateInscription());

        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(role -> role.getNomRole())
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    @Transactional(readOnly = true)  // ← AJOUTER @Transactional
    public String generateToken(UserDetails userDetails) {
        // ✅ Utiliser findByEmailWithRoles au lieu de findByEmail
        Users user = userRepository.findByEmailWithRoles(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + userDetails.getUsername()));

        UserTokenDto userToken = convertToTokenDto(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("user", userToken);
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtkey));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token, UserDetails user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}