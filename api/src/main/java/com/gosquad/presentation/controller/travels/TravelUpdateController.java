package com.gosquad.presentation.controller.travels;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;
import com.gosquad.usecase.travels.TravelUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class TravelUpdateController {
    
    private final TravelUpdateService travelUpdateService;

    public TravelUpdateController(TravelUpdateService travelUpdateService) {
        this.travelUpdateService = travelUpdateService;
    }

    @PutMapping("/travel/{id}")
    public ResponseEntity<String> updateTravel(@PathVariable String id, @RequestBody VoyageRequestDTO travelRequestDTO) {
        try {
            // Extract numeric ID from the uniqueTravelId (similar to TravelGetServiceImpl)
            int numericId;
            try {
                // Try to extract numeric part - assume format is "COMPANYCODE" + numeric ID
                String numericPart = id.replaceAll("[A-Z]+", ""); // Remove alphabetic company code
                if (numericPart.isEmpty()) {
                    return ResponseEntity.badRequest().body("Format d'ID invalide: " + id);
                }
                numericId = Integer.parseInt(numericPart);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("ID de voyage invalide: " + id);
            }
            
            travelUpdateService.updateTravelFromDTO(numericId, travelRequestDTO);
            return ResponseEntity.ok("Voyage mis à jour avec succès");
        } catch (ConstraintViolationException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Erreur serveur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur inattendue : " + e.getMessage());
        }
    }

    @DeleteMapping("/travel/{id}")
    public ResponseEntity<String> deleteTravel(@PathVariable String id) {
        try {
            // Extract numeric ID from the uniqueTravelId
            int numericId;
            try {
                String numericPart = id.replaceAll("[A-Z]+", ""); // Remove alphabetic company code
                if (numericPart.isEmpty()) {
                    return ResponseEntity.badRequest().body("Format d'ID invalide: " + id);
                }
                numericId = Integer.parseInt(numericPart);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("ID de voyage invalide: " + id);
            }
            
            travelUpdateService.deleteTravel(numericId);
            return ResponseEntity.ok("Voyage supprimé avec succès");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Erreur serveur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur inattendue : " + e.getMessage());
        }
    }
}
