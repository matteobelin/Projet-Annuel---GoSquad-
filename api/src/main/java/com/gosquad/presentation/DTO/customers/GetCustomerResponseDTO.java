package com.gosquad.presentation.DTO.customers;

import lombok.Data;

import java.sql.Date;

@Data
public class GetCustomerResponseDTO {
    private String uniqueCustomerId; // companyCode + customerId
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
    private byte[] idCard;
    private byte[] passport;

    public GetCustomerResponseDTO(String uniqueCustomerId,
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
                                  byte[] passport) {
        this.uniqueCustomerId = uniqueCustomerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.idCardNumber = idCardNumber;
        this.idCardExpirationDate = idCardExpirationDate;
        this.passportNumber = passportNumber;
        this.passportExpirationDate = passportExpirationDate;
        this.isoNationality = isoNationality;
        this.addressLine = addressLine;
        this.cityName = cityName;
        this.postalCode = postalCode;
        this.isoCode = isoCode;
        this.addressLineBilling = addressLineBilling;
        this.cityNameBilling = cityNameBilling;
        this.postalCodeBilling = postalCodeBilling;
        this.isoCodeBilling = isoCodeBilling;
        this.companyCode = companyCode;
        this.idCard = idCard;
        this.passport = passport;
    }

}
