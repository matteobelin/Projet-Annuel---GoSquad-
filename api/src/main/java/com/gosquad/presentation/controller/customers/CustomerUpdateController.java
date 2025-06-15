package com.gosquad.presentation.controller.customers;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.presentation.DTO.customers.CustomerUpdateDTO;
import com.gosquad.usecase.customers.CustomerUpdateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;

import java.util.Map;


@Controller
public class CustomerUpdateController {
    private final JWTInterceptor jwtInterceptor;
    private final CustomerUpdateService customerUpdateService;

    public CustomerUpdateController(JWTInterceptor jwtInterceptor, CustomerUpdateService customerUpdateService) {
        this.jwtInterceptor = jwtInterceptor;
        this.customerUpdateService = customerUpdateService;
    }

    @PutMapping("/updateCustomer")
    public ResponseEntity<?> updateCustomer(HttpServletRequest request, @RequestBody CustomerUpdateDTO body) {
        return handleUpdate(() -> {
            String companyCode = null;
            try {
                companyCode = getCompanyCodeFromRequest(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                customerUpdateService.updateCustomer(body, companyCode);
            } catch (SQLException | ConstraintViolationException | NotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @PutMapping("/updateCustomerPassport")
    public ResponseEntity<?> updateCustomerPassport(HttpServletRequest request,
                                                    @RequestPart("dto") CustomerUpdateDTO body,
                                                    @RequestPart("file") MultipartFile passport) {
        return handleUpdate(() -> {
            String companyCode = null;
            try {
                companyCode = getCompanyCodeFromRequest(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                customerUpdateService.updateCustomerPassport(body, passport, companyCode);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @PutMapping("/updateCustomerIdCard")
    public ResponseEntity<?> updateCustomerIdCard(HttpServletRequest request,
                                                  @RequestPart("dto") CustomerUpdateDTO body,
                                                  @RequestPart("file") MultipartFile idCard) {
        return handleUpdate(() -> {
            String companyCode = null;
            try {
                companyCode = getCompanyCodeFromRequest(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                customerUpdateService.updateCustomerIdCard(body, idCard, companyCode);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @PutMapping("/updateCustomerToAnonymous")
    public ResponseEntity<?> anonymizeCustomer(HttpServletRequest request, @RequestBody CustomerUpdateDTO body) {
        return handleUpdate(() -> {
            String companyCode = null;
            try {
                companyCode = getCompanyCodeFromRequest(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                customerUpdateService.anonymizeCustomer(body, companyCode);
            } catch (SQLException | NotFoundException | ConstraintViolationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private ResponseEntity<?> handleUpdate(Runnable updateAction) {
        try {
            updateAction.run();
            return ResponseEntity.ok("Opération réalisée avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur : " + e.getMessage());
        }
    }

    private String getCompanyCodeFromRequest(HttpServletRequest request) throws Exception {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);
        return tokenInfo.get("companyCode").toString();
    }
}
