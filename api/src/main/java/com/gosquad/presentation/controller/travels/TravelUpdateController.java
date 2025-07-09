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
    public ResponseEntity<String> updateTravel(@PathVariable int id, @RequestBody VoyageRequestDTO travelRequestDTO) {
        try {
            travelUpdateService.updateTravelFromDTO(id, travelRequestDTO);
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
    public ResponseEntity<String> deleteTravel(@PathVariable int id) {
        try {
            travelUpdateService.deleteTravel(id);
            return ResponseEntity.ok("Voyage supprimé avec succès");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Erreur serveur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur inattendue : " + e.getMessage());
        }
    }
}
