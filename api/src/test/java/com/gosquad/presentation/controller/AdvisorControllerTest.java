package com.gosquad.presentation.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.advisors.AdvisorEntity;
import com.gosquad.usecase.advisors.AdvisorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.SQLException;
import java.util.Arrays;

public class AdvisorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdvisorService advisorService;

    @InjectMocks
    private AdvisorController advisorController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(advisorController).build();
    }

    @Test
    public void testGetAllAdvisors_Success() throws Exception {
        AdvisorEntity advisor1 = new AdvisorEntity(1, "John", "Doe", "john@example.com", "123456789", "ROLE_USER");
        AdvisorEntity advisor2 = new AdvisorEntity(2, "Jane", "Smith", "jane@example.com", "987654321", "ROLE_ADMIN");

        when(advisorService.getAllAdvisors()).thenReturn(Arrays.asList(advisor1, advisor2));

        mockMvc.perform(get("/getAllAdvisor"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void testGetAllAdvisors_DbError() throws Exception {
        when(advisorService.getAllAdvisors()).thenThrow(new SQLException("DB failure"));

        mockMvc.perform(get("/getAllAdvisor"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error: DB failure"));
    }

    @Test
    public void testGetAdvisorById_Success() throws Exception {
        AdvisorEntity advisor = new AdvisorEntity(1, "John", "Doe", "john@example.com", "123456789", "ROLE_USER");

        when(advisorService.getAdvisorById(1)).thenReturn(advisor);

        mockMvc.perform(get("/getAdvisor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testGetAdvisorById_NotFound() throws Exception {
        when(advisorService.getAdvisorById(1)).thenThrow(new NotFoundException("Advisor not found"));

        mockMvc.perform(get("/getAdvisor/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Error: Advisor not found"));
    }

    @Test
    public void testGetAdvisorById_DbError() throws Exception {
        when(advisorService.getAdvisorById(1)).thenThrow(new SQLException("DB failure"));

        mockMvc.perform(get("/getAdvisor/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error: DB failure"));
    }
}
