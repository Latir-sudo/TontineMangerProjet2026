package com.tontineApp.tontine_manager.service;

import com.tontineApp.tontine_manager.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class JwtService {


    private final UserRepository userRepository;

    private String jwtkey = "QiAT1hs6emp0qZZqikMrGcJVl/h8cULpkyJfp/cZlgHhsBtlX3iaACqNMA7EnW55pNN4oqppetIcHdZw7hfVhA==";


    // recupération de l'utilisateur connecté comme un claims
    public String generateToken(UserDetails user) {
        var user1= userRepository.findByEmail(user.getUsername()).get();
        Map<String,Object> claims1 = new HashMap<>();
        claims1.put("user",user1);
        Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims1)
                .issuer("self")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+86400000))
                .subject(user.getUsername()).signWith(getSignKey()).compact();

    }
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtkey));
    }


    private Claims extractAllClaims (String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // extraction du username à partir du token

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token,UserDetails user){
        final String username=extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // Extraire l'expiration




}

