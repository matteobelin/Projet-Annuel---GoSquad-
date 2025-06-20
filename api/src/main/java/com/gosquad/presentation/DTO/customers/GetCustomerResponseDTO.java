package com.gosquad.presentation.DTO.customers;

import java.sql.Date;

public record GetCustomerResponseDTO(
        String uniqueCustomerId,  // companyCode + customerId
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Date birthDate,
        String idCardNumber,
        Date idCardExpirationDate,
        String passportNumber,
        Date passportExpirationDate,
        String isoNationality,
        String addressLine,
        String cityName,
        String postalCode,
        String isoCode,
        String addressLineBilling,
        String cityNameBilling,
        String postalCodeBilling,
        String isoCodeBilling,
        String companyCode,
        byte[] idCard,
        byte[] passport
) {}
