package com.gosquad.presentation.DTO.customers;

import lombok.Data;

@Data
public class CustomerResponseDTO {
    private String uniqueCustomerId; // companyCode + customerId
    private String firstName;
    private String lastName;
    private String email;

    public CustomerResponseDTO(String uniqueCustomerId, String firstName, String lastName, String email) {
        this.uniqueCustomerId = uniqueCustomerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}