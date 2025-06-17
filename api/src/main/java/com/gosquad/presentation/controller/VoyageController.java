package com.gosquad.presentation.controller;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.voyages.VoyageEntity;
import com.gosquad.presentation.DTO.VoyageRequestDTO;
import com.gosquad.usecase.voyages.VoyageFicheService;
import com.gosquad.usecase.voyages.VoyageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/voyages")
public class VoyageController {
    
    private final VoyageService voyageService;
    private final VoyageFicheService voyageFicheService;

    public VoyageController(VoyageService voyageService, VoyageFicheService voyageFicheService) {
        this.voyageService = voyageService;
        this.voyageFicheService = voyageFicheService;
    }

    @GetMapping
    public ResponseEntity<?> getAllVoyages() {
        try {
            List<VoyageEntity> voyages = voyageService.getAllVoyages();
            return ResponseEntity.ok(voyages);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVoyageById(@PathVariable int id) {
        try {
            VoyageEntity voyage = voyageService.getVoyageById(id);
            return ResponseEntity.ok(voyage);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error: " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> getVoyagesByClientId(@PathVariable int clientId) {
        try {
            List<VoyageEntity> voyages = voyageService.getVoyagesByClientId(clientId);
            return ResponseEntity.ok(voyages);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error: " + e.getMessage());
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createVoyage(@RequestBody VoyageRequestDTO request) {
        try {
            VoyageEntity voyage = new VoyageEntity();
            voyage.setTitre(request.getTitre());
            voyage.setDestination(request.getDestination());
            voyage.setDateDepart(request.getDateDepart());
            voyage.setDateRetour(request.getDateRetour());
            voyage.setNombreParticipants(request.getNombreParticipants());
            voyage.setBudget(request.getBudget());
            voyage.setClientId(request.getClientId());
            voyage.setStatut(request.getStatut());

            VoyageEntity createdVoyage = voyageService.createVoyage(voyage);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVoyage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVoyage(@PathVariable int id, @RequestBody VoyageRequestDTO request) {
        try {
            VoyageEntity voyage = new VoyageEntity();
            voyage.setId(id);
            voyage.setTitre(request.getTitre());
            voyage.setDestination(request.getDestination());
            voyage.setDateDepart(request.getDateDepart());
            voyage.setDateRetour(request.getDateRetour());
            voyage.setNombreParticipants(request.getNombreParticipants());
            voyage.setBudget(request.getBudget());
            voyage.setClientId(request.getClientId());
            voyage.setStatut(request.getStatut());

            VoyageEntity updatedVoyage = voyageService.updateVoyage(voyage);
            return ResponseEntity.ok(updatedVoyage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error: " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVoyage(@PathVariable int id) {
        try {
            voyageService.deleteVoyage(id);
            return ResponseEntity.ok().body("Voyage deleted successfully");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error: " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}/fiche")
    public ResponseEntity<?> generateFicheVoyage(@PathVariable int id) {
        try {
            String ficheHTML = voyageFicheService.generateFicheHTML(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(ficheHTML);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error: " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/fiche/pdf")
    public ResponseEntity<byte[]> downloadFichePDF(@PathVariable int id) {
        try {
            byte[] pdfContent = voyageFicheService.generateFichePDF(id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "fiche-voyage-" + id + ".pdf");
            headers.setContentLength(pdfContent.length);
            
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}