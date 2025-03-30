package com.gosquad.presentation.controller;
import com.gosquad.domain.advisors.AdvisorEntity;
import com.gosquad.domain.advisors.AdvisorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;


@RestController
public class AdvisorController {
    private final AdvisorService advisorService;


    public AdvisorController(AdvisorService advisorService) {
        this.advisorService = advisorService;
    }
    @GetMapping("/getAllAdvisor")
    public ResponseEntity<List<AdvisorEntity>> getAllAdvisors() {
        try {
            List<AdvisorEntity> advisors = advisorService.getAllAdvisors();
            return ResponseEntity.ok(advisors);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
