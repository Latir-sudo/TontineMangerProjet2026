package com.tontineApp.tontine_manager.configuration;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.tontineApp.tontine_manager.service.CustomUserDetailService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SpringSecurityConfig {

    private String jwtkey = "JEPgVbobKMbB6RBqkAeeQ43xykgbxULovhI9wW9cVRS";


    private final CustomUserDetailService customUserDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer((oauth2)->oauth2.jwt(Customizer.withDefaults()))
                // API REST stateless

                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/v3/api-docs.yaml",
                                        "/login"
                                ).permitAll()

// ── Auth ─────────────────────────────────────────
                                .requestMatchers(HttpMethod.POST,"/api/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/users").permitAll()
                                .anyRequest().authenticated()

                )
// Branchement de handler

        .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    // méthode permettant de décoder un token jwt
    @Bean
    public JwtDecoder jwtDecoder(){
        SecretKeySpec secretkey = new SecretKeySpec(this.jwtkey.getBytes(),0,this.jwtkey.getBytes().length,"RSA");
        return NimbusJwtDecoder.withSecretKey(secretkey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtkey.getBytes()));
    }




}
