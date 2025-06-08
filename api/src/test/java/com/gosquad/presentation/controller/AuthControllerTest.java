package com.gosquad.presentation.controller;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gosquad.usecase.auth.AdvisorAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.SQLException;

public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdvisorAuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testLogin_Success() throws Exception {
        String email = "user@example.com";
        String password = "pass";
        String companyCode = "COMP";

        when(authService.authentification(email, password, companyCode)).thenReturn(true);
        when(authService.generateJwtToken(email, companyCode)).thenReturn("fake-jwt-token");

        String jsonBody = """
            {
                "email": "user@example.com",
                "password": "pass",
                "companyCode": "COMP"
            }
            """;

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer fake-jwt-token"))
                .andExpect(content().string("Connexion réussie"));
    }

    @Test
    public void testLogin_FailAuthentication() throws Exception {
        String email = "user@example.com";
        String password = "wrongpass";
        String companyCode = "COMP";

        when(authService.authentification(email, password, companyCode)).thenReturn(false);

        String jsonBody = """
            {
                "email": "user@example.com",
                "password": "wrongpass",
                "companyCode": "COMP"
            }
            """;

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Échec de l'authentification : code entreprise, email ou mot de passe invalide"));
    }

    @Test
    public void testLogin_ExceptionHandling() throws Exception {
        String email = "user@example.com";
        String password = "pass";
        String companyCode = "COMP";

        when(authService.authentification(email, password, companyCode)).thenThrow(new SQLException("DB error"));

        String jsonBody = """
            {
                "email": "user@example.com",
                "password": "pass",
                "companyCode": "COMP"
            }
            """;

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isInternalServerError());
    }
}
