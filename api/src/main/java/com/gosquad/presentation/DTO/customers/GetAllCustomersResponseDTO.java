package com.gosquad.presentation.DTO.customers;

public record GetAllCustomersResponseDTO(
        String uniqueCustomerId, // companyCode + customerId
        String firstName,
        String lastName,
        String email
) {}