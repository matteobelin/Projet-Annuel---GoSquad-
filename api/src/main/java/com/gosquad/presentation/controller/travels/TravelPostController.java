package com.gosquad.presentation.controller.travels;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;
import com.gosquad.usecase.travels.TravelPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class TravelPostController {
    
    private final TravelPostService travelPostService;

    public TravelPostController(TravelPostService travelPostService) {
        this.travelPostService = travelPostService;
    }

    @PostMapping("/travel")
    public ResponseEntity<String> addTravel(@RequestBody VoyageRequestDTO travelRequestDTO) {
        try {
            travelPostService.createTravelFromDTO(travelRequestDTO);
            return ResponseEntity.ok("Voyage ajouté avec succès");
        } catch (ConstraintViolationException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Erreur serveur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur inattendue : " + e.getMessage());
        }
    }
}
