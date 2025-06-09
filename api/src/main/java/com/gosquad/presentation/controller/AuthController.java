package com.gosquad.presentation.controller;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.presentation.DTO.AuthRequest;
import com.gosquad.usecase.auth.AdvisorAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;


@RestController
public class AuthController {
    private final AdvisorAuthService authService;

    public AuthController(AdvisorAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest body){
        try {
            if (authService.authentification(body.getEmail(), body.getPassword(), body.getCompanyCode())) {
                String jwtToken = authService.generateJwtToken(body.getEmail(), body.getCompanyCode());

                return ResponseEntity.ok()
                        .header("Authorization", "Bearer " + jwtToken)
                        .body("Connexion réussie");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Échec de l'authentification : code entreprise, email ou mot de passe invalide");
            }
        } catch (SQLException | NotFoundException | ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }
}
