package com.gosquad.presentation.controller.customers;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.presentation.DTO.customers.CustomerRequestDTO;
import com.gosquad.usecase.customers.CustomerPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;

@RestController
public class CustomerPostController {
    private final CustomerPostService customerUseCase;

    public CustomerPostController(CustomerPostService customerUseCase) {
        this.customerUseCase = customerUseCase;
    }



    @PostMapping("/customer")
    public ResponseEntity<String> addCustomer(@RequestPart("customer") CustomerRequestDTO customerRequestDTO,
                                              @RequestPart(value = "idCard", required = false) MultipartFile idCard,
                                              @RequestPart(value = "passport", required = false) MultipartFile passport) {
        try {
            customerUseCase.createCustomer(customerRequestDTO, idCard, passport);
            return ResponseEntity.ok("Client ajouté avec succès");
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Ressource non trouvée : " + e.getMessage());
        } catch (ConstraintViolationException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        } catch (IOException | SQLException e) {
            return ResponseEntity.status(500).body("Erreur serveur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur inattendue : " + e.getMessage());
        }
    }


}
