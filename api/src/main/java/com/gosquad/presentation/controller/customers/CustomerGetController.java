package com.gosquad.presentation.controller.customers;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.presentation.DTO.customers.GetCustomerResponseDTO;

import com.gosquad.usecase.customers.CustomerGetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.gosquad.presentation.DTO.customers.GetAllCustomersResponseDTO;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@Controller
public class CustomerGetController {


    private final CustomerGetService customerGetService;

    public CustomerGetController(CustomerGetService customerGetService) {
        this.customerGetService = customerGetService;
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<?> getAllCustomer(HttpServletRequest request){
        try {
            List<GetAllCustomersResponseDTO> customers = customerGetService.getAllCustomers(request);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur : " + e.getMessage());
        }
    }

    // Endpoint moderne pour le frontend
    @GetMapping("/api/clients")
    public ResponseEntity<?> getAllClients(HttpServletRequest request){
        try {
            List<GetAllCustomersResponseDTO> customers = customerGetService.getAllCustomers(request);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/getCustomer")
    public ResponseEntity<?> getCustomer(HttpServletRequest request){
        try {
            GetCustomerResponseDTO customer = customerGetService.getCustomer(request);
            return ResponseEntity.ok(customer);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Erreur base de données : " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Non trouvé : " + e.getMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Violation de contrainte : " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur d'entrée/sortie : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur inconnue : " + e.getMessage());
        }
    }

    // Endpoint moderne pour le frontend
    @GetMapping("/api/clients/{id}")
    public ResponseEntity<?> getClient(@PathVariable String id, HttpServletRequest request){
        try {
            // Le service attend le paramètre "customerNumber" comme query parameter
            // On utilise un wrapper pour modifier la requête
            HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request) {
                @Override
                public String getParameter(String name) {
                    if ("customerNumber".equals(name)) {
                        return id;
                    }
                    return super.getParameter(name);
                }
            };
            
            GetCustomerResponseDTO customer = customerGetService.getCustomer(requestWrapper);
            return ResponseEntity.ok(customer);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Erreur base de données : " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Non trouvé : " + e.getMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Violation de contrainte : " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur d'entrée/sortie : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur inconnue : " + e.getMessage());
        }
    }
}
