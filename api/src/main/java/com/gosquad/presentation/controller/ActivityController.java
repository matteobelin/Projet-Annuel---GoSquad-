package com.gosquad.presentation.controller;

import com.gosquad.presentation.DTO.activities.ActivityRequestDTO;
import com.gosquad.presentation.DTO.activities.ActivityResponseDTO;
import com.gosquad.usecase.activities.ActivityDeleteService;
import com.gosquad.usecase.activities.ActivityGetService;
import com.gosquad.usecase.activities.ActivityPostService;
import com.gosquad.usecase.activities.ActivityUpdateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActivityController {

    private final ActivityGetService activityGetService;
    private final ActivityPostService activityPostService;
    private final ActivityUpdateService activityUpdateService;
    private final ActivityDeleteService activityDeleteService;


    public ActivityController(ActivityGetService activityGetService, ActivityPostService activityPostService, ActivityUpdateService activityUpdateService, ActivityDeleteService activityDeleteService) {
        this.activityGetService = activityGetService;
        this.activityPostService = activityPostService;
        this.activityUpdateService = activityUpdateService;
        this.activityDeleteService = activityDeleteService;
    }

    @GetMapping("/activity/all")
    public ResponseEntity<List<ActivityResponseDTO>> getAllActivities(HttpServletRequest request) {
        try {
            List<ActivityResponseDTO> activities = activityGetService.getAllActivities(request);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/activity/by-category")
    public ResponseEntity<List<ActivityResponseDTO>> getActivitiesByCategory(HttpServletRequest request) {
        try {
            List<ActivityResponseDTO> activities = activityGetService.getAllActivitiesByCategory(request);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/activity/by-id")
    public ResponseEntity<ActivityResponseDTO> getActivityById(HttpServletRequest request) {
        try {
            ActivityResponseDTO activity = activityGetService.getActivityById(request);
            return ResponseEntity.ok(activity);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/activity")
    public ResponseEntity<String> createActivity(
            HttpServletRequest request,
            @RequestBody ActivityRequestDTO dto) {
        try {
            activityPostService.createActivity(request, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Activity created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @PutMapping("/activity")
    public ResponseEntity<String> updateActivity(
            HttpServletRequest request,
            @RequestBody ActivityRequestDTO dto) {
        try {
            activityUpdateService.updateActivity(request, dto);
            return ResponseEntity.ok("Activity updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @DeleteMapping("/activity")
    public ResponseEntity<String> deleteActivity(
            HttpServletRequest request,
            @RequestBody ActivityRequestDTO dto) {
        try {
            activityDeleteService.deleteActivity(request, dto);
            return ResponseEntity.ok("Activity deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
