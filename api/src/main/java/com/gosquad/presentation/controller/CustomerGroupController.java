package com.gosquad.presentation.controller;

import com.gosquad.usecase.customergroup.CustomerGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerGroupController {

    private final CustomerGroupService customerGroupService;

    @Autowired
    public CustomerGroupController(CustomerGroupService customerGroupService) {
        this.customerGroupService = customerGroupService;
    }

    @PostMapping("/{groupId}/add-customer")
    public ResponseEntity<?> addCustomerToGroup(
            @PathVariable Integer groupId,
            @RequestBody AddCustomerToGroupRequest request) {
        try {
            customerGroupService.addCustomerToGroup(request.customerId, groupId);
            return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body("{\"message\": \"Client ajouté au groupe avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .header("Content-Type", "application/json")
                .body("{\"error\": \"Erreur lors de l'ajout du client au groupe: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{groupId}/remove-customer/{customerId}")
    public ResponseEntity<?> removeCustomerFromGroup(
            @PathVariable Integer groupId,
            @PathVariable Integer customerId) {
        try {
            customerGroupService.removeCustomerFromGroup(customerId, groupId);
            // Renvoie un objet JSON simple et explicite
            return ResponseEntity.ok().header("Content-Type", "application/json")
                .body("{\"message\": \"Client retiré du groupe avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .header("Content-Type", "application/json")
                .body("{\"error\": \"Erreur lors de la suppression du client du groupe: " + e.getMessage() + "\"}");
        }
    }

    // DTO for request body
    public static class AddCustomerToGroupRequest {
        public int customerId;

        public AddCustomerToGroupRequest() {}

        public AddCustomerToGroupRequest(int customerId) {
            this.customerId = customerId;
        }
    }
}
