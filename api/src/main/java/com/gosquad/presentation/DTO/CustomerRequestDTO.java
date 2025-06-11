package com.gosquad.presentation.DTO;

import lombok.Data;

import java.sql.Date;

@Data
public class CustomerRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date birthDate;
    private String idCardNumber;
    private Date idCardExpirationDate;
    private String passportNumber;
    private Date passportExpirationDate;
    private String isoNationality;
    private String addressLine;
    private String cityName;
    private String postalCode;
    private String isoCode;
    private String addressLineBilling;
    private String cityNameBilling;
    private String postalCodeBilling;
    private String isoCodeBilling;
    private String companyCode;
}
