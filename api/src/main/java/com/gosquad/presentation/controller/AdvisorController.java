package com.gosquad.presentation.controller;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.advisors.AdvisorEntity;
import com.gosquad.usecase.advisors.AdvisorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<?> getAllAdvisors() {
        try {
            List<AdvisorEntity> advisors = advisorService.getAllAdvisors();
            return ResponseEntity.ok(advisors);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Database error: " + e.getMessage());
        }
    }

    @GetMapping("/getAdvisor/{id}")
    public ResponseEntity<?> getAdvisorById(@PathVariable int id) {
        try {
            AdvisorEntity advisor = advisorService.getAdvisorById(id);
            return ResponseEntity.ok(advisor);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error: " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }
}
