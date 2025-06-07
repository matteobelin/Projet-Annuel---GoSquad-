package com.gosquad.presentation.controller;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.advisors.auth.AdvisorAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
public class AuthController {
    private final AdvisorAuthService authService;

    public AuthController(AdvisorAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, Object> body) throws SQLException, NotFoundException, ConstraintViolationException {
        String email = (String) body.get("email");
        String password = (String) body.get("password");
        String companyCode = (String) body.get("companyCode");

        if (authService.authentification(email, password, companyCode)) {
            String jwtToken = authService.generateJwtToken(email, companyCode);

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + jwtToken)
                    .body("Connexion réussie");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Échec de l'authentification");
        }
    }
}
