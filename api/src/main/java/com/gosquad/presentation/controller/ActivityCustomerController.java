package com.gosquad.presentation.controller;

import com.gosquad.presentation.DTO.activities_customers.ActivityCustomerRequestDTO;
import com.gosquad.presentation.DTO.activities_customers.ActivityCustomerResponseDTO;
import com.gosquad.usecase.activities_customers.ActivityCustomerGetService;
import com.gosquad.usecase.activities_customers.ActivityCustomerPostService;
import com.gosquad.usecase.activities_customers.ActivityCustomerUpdateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActivityCustomerController {

    private final ActivityCustomerUpdateService activityCustomerUpdateService;
    private final ActivityCustomerGetService activityCustomerGetService;
    private final ActivityCustomerPostService activityCustomerPostService;

    public ActivityCustomerController(ActivityCustomerUpdateService activityCustomerUpdateService, ActivityCustomerGetService activityCustomerGetService, ActivityCustomerPostService activityCustomerPostService) {
        this.activityCustomerUpdateService = activityCustomerUpdateService;
        this.activityCustomerGetService = activityCustomerGetService;
        this.activityCustomerPostService = activityCustomerPostService;
    }

    @GetMapping("/customerActivity/by-group/participation")
    public ResponseEntity<?> getActivitiesByGroupIdWhereParticipation(HttpServletRequest request) {
        try {
            List<ActivityCustomerResponseDTO> result = activityCustomerGetService.getActivitiesByGroupIdWhereParticipation(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/customerActivity/by-customer")
    public ResponseEntity<?> getActivityByCustomerId(HttpServletRequest request) {
        try {
            List<ActivityCustomerResponseDTO> result = activityCustomerGetService.getActivityByCustomerId(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/customerActivity/by-customer/participation")
    public ResponseEntity<?> getActivityByCustomerWhereParticipation(HttpServletRequest request) {
        try {
            List<ActivityCustomerResponseDTO> result = activityCustomerGetService.getActivityByCustomerWhereParticipation(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/customerActivity/by-id")
    public ResponseEntity<?> getActivityById(HttpServletRequest request) {
        try {
            ActivityCustomerResponseDTO result = activityCustomerGetService.getActivityById(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/customerActivity/by-customer-group/participation")
    public ResponseEntity<?> getActivityByCustomerIdAndGroupIdWhereParticipation(HttpServletRequest request) {
        try {
            List<ActivityCustomerResponseDTO> result = activityCustomerGetService.getActivityByCustomerIdAndGroupIdWhereParticipation(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/customerActivity/by-activity-group")
    public ResponseEntity<?> getCustomersByActivityIdAndGroupId(HttpServletRequest request) {
        try {
            List<ActivityCustomerResponseDTO> result = activityCustomerGetService.getCustomersByActivityIdAndGroupId(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @PostMapping("/customerActivity")
    public ResponseEntity<?> createActivityCustomer(HttpServletRequest request, @RequestBody ActivityCustomerRequestDTO dto) {
        try {
            activityCustomerPostService.createActivityCustomer(request, dto);
            return ResponseEntity.ok("Création réussie");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création : " + e.getMessage());
        }
    }

    @PutMapping("/customerActivity")
    public ResponseEntity<?> updateActivityCustomer(HttpServletRequest request, @RequestBody ActivityCustomerRequestDTO dto) {
        try {
            activityCustomerUpdateService.updateActivityCustomer(request, dto);
            return ResponseEntity.ok("Mise à jour réussie");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

}
