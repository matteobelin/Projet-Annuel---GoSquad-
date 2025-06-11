package com.gosquad.presentation.controller.customers;

import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.customers.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.gosquad.presentation.DTO.customers.CustomerResponseDTO;

import java.util.List;
import java.util.Map;

@Controller
public class CustomerGetController {

    private final CustomerService customerService;
    private final CompanyService companyService;
    private final JWTInterceptor jwtInterceptor;

    public CustomerGetController(CustomerService customerService, CompanyService companyService, JWTInterceptor jwtInterceptor) {
        this.customerService = customerService;
        this.companyService = companyService;
        this.jwtInterceptor = jwtInterceptor;
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<?> getAllCustomer(HttpServletRequest request){
        try{
            String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(7);
            Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);

            String companyCode = tokenInfo.get("companyCode").toString();

            CompanyEntity company = companyService.getCompanyByCode(companyCode);
            List<CustomerEntity> customers = customerService.getAllCustomers(company.getId());

            List<CustomerResponseDTO> responseList = customers.stream().map(customer -> {
                String uniqueId = companyCode + customer.getId();
                return new CustomerResponseDTO(
                        uniqueId,
                        customer.getFirstname(),
                        customer.getLastname(),
                        customer.getEmail()
                );
            }).toList();

            return ResponseEntity.ok(responseList);

        }catch(Exception e){
            return ResponseEntity.internalServerError()
                    .body("Database error: " + e.getMessage());
        }
    }
}
