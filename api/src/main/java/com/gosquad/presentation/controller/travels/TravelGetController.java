package com.gosquad.presentation.controller.travels;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.presentation.DTO.travels.GetTravelResponseDTO;
import com.gosquad.presentation.DTO.travels.GetAllTravelsResponseDTO;
import com.gosquad.usecase.travels.TravelGetService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;
import java.util.List;

@Controller
public class TravelGetController {

    private final TravelGetService travelGetService;

    public TravelGetController(TravelGetService travelGetService) {
        this.travelGetService = travelGetService;
    }

    @GetMapping("/getAllTravels")
    public ResponseEntity<?> getAllTravels(HttpServletRequest request) {
        try {
            List<GetAllTravelsResponseDTO> travels = travelGetService.getAllTravels(request);
            return ResponseEntity.ok(travels);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/getTravel")
    public ResponseEntity<?> getTravel(HttpServletRequest request) {
        try {
            GetTravelResponseDTO travel = travelGetService.getTravel(request);
            return ResponseEntity.ok(travel);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Erreur base de données : " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Non trouvé : " + e.getMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Violation de contrainte : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur : " + e.getMessage());
        }
    }
}
