package com.gosquad.presentation.controller;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.advisors.AdvisorEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.usecase.advisors.AdvisorService;
import com.gosquad.usecase.company.CompanyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;


@RestController
public class AdvisorController {
    private final AdvisorService advisorService;
    private final CompanyService companyService;


    public AdvisorController(AdvisorService advisorService, CompanyService companyService) {
        this.advisorService = advisorService;
        this.companyService = companyService;
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

    @GetMapping("/getAdvisor")
    public ResponseEntity<?> getAdvisorById(HttpServletRequest request) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            AdvisorEntity advisor = advisorService.getAdvisorById(id);
            CompanyEntity company = companyService.getCompanyById(advisor.getCompanyId());
            return ResponseEntity.ok(java.util.Map.of(
                    "id", advisor.getId(),
                    "firstname", advisor.getFirstname(),
                    "lastname", advisor.getLastname(),
                    "email", advisor.getEmail(),
                    "phone", advisor.getPhone(),
                    "role", advisor.getRole(),
                    "companyCode", company.getCode()
            ));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error: " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }
}
