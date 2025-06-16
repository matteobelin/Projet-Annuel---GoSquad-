package com.gosquad.usecase.customers.utils;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.domain.cities.CityEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.presentation.DTO.customers.CustomerRequestDTO;
import com.gosquad.usecase.addresses.AddressService;
import com.gosquad.usecase.cities.CityService;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.countries.CountryService;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.SQLException;


@Component
public class CustomerValidationHelper {

    private final CountryService countryService;
    private final AddressService addressService;
    private final CityService cityService;
    private final CompanyService companyService;

    public CustomerValidationHelper(CountryService countryService,
                                    AddressService addressService,
                                    CityService cityService,
                                    CompanyService companyService) {
        this.countryService = countryService;
        this.addressService = addressService;
        this.cityService = cityService;
        this.companyService = companyService;
    }

    @Data
    public static class ValidatedCustomerData {
        private final CompanyEntity company;
        private final CountryEntity nationalityCountry;
        private final CountryEntity addressCountry;
        private final CountryEntity billingCountry;
        private final CityEntity city;
        private final CityEntity billingCity;
        private final AddressEntity address;
        private final AddressEntity billingAddress;
    }

    public ValidatedCustomerData validateAndPrepareCustomerData(CustomerRequestDTO customerData)
            throws SQLException, NotFoundException, ConstraintViolationException {

        // Validation des codes ISO
        validateIsoCode(customerData.isoNationality(), "code ISO de nationalité");
        validateIsoCode(customerData.isoCode(), "code ISO d'adresse");
        validateIsoCode(customerData.isoCodeBilling(), "code ISO de facturation");

        // Récupération des entités
        CompanyEntity company = companyService.getCompanyByCode(customerData.companyCode());

        CountryEntity nationalityCountry = countryService.getCountryByIsoCode(customerData.isoNationality());
        CountryEntity addressCountry = countryService.getCountryByIsoCode(customerData.isoCode());
        CountryEntity billingCountry = countryService.getCountryByIsoCode(customerData.isoCodeBilling());

        CityEntity city = cityService.getOrCreateCity(
                customerData.cityName(),
                customerData.postalCode(),
                addressCountry.getId()
        );

        CityEntity billingCity = cityService.getOrCreateCity(
                customerData.cityNameBilling(),
                customerData.postalCodeBilling(),
                billingCountry.getId()
        );

        AddressEntity address = addressService.getOrCreateAddress(
                customerData.addressLine(),
                city.getId()
        );

        AddressEntity billingAddress = addressService.getOrCreateAddress(
                customerData.addressLineBilling(),
                billingCity.getId()
        );

        return new ValidatedCustomerData(
                company, nationalityCountry, addressCountry, billingCountry,
                city, billingCity, address, billingAddress
        );
    }

    private void validateIsoCode(String code, String fieldName) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Le " + fieldName + " ne peut pas être null ou vide");
        }
    }

    }