package com.gosquad.usecase.customers.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.domain.cities.CityEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.presentation.DTO.customers.CustomerRequestDTO;
import com.gosquad.usecase.addresses.AddressService;
import com.gosquad.usecase.cities.CityService;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.countries.CountryService;
import com.gosquad.usecase.customers.CustomerService;
import com.gosquad.usecase.customers.CustomerPostService;
import com.gosquad.usecase.files.FileService;
import com.gosquad.usecase.security.EncryptionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class CustomerPostServiceImpl implements CustomerPostService {

    private final EncryptionService encryptionService;
    private final FileService fileService;
    private final CountryService countryService;
    private final AddressService addressService;
    private final CityService cityService;
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final JWTInterceptor jwtInterceptor;

    public CustomerPostServiceImpl(EncryptionService encryptionService, FileService fileService, CountryService countryService,
                                   AddressService addressService, CityService cityService, CustomerService customerService,
                                   CompanyService companyService, JWTInterceptor jwtInterceptor) {
        this.encryptionService = encryptionService;
        this.fileService = fileService;
        this.countryService = countryService;
        this.addressService = addressService;
        this.cityService = cityService;
        this.customerService = customerService;
        this.companyService = companyService;
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void createCustomer(CustomerRequestDTO customerRequestDTO, MultipartFile idCard, MultipartFile passport)
            throws IOException, SQLException, ConstraintViolationException, NotFoundException {

        validateIsoCode(customerRequestDTO.isoNationality(), "code ISO de nationalité");
        validateIsoCode(customerRequestDTO.isoCode(), "code ISO d'adresse");
        validateIsoCode(customerRequestDTO.isoCodeBilling(), "code ISO de facturation");

        CompanyEntity companyEntity = companyService.getCompanyByCode(customerRequestDTO.companyCode());

        CountryEntity nationalityCountry = countryService.getCountryByIsoCode(customerRequestDTO.isoNationality());
        CountryEntity addressCountry = countryService.getCountryByIsoCode(customerRequestDTO.isoCode());
        CountryEntity billingCountry = countryService.getCountryByIsoCode(customerRequestDTO.isoCodeBilling());

        CityEntity city = cityService.getOrCreateCity(customerRequestDTO.cityName(),
                customerRequestDTO.postalCode(), addressCountry.getId());

        CityEntity billingCity = cityService.getOrCreateCity(customerRequestDTO.cityNameBilling(),
                customerRequestDTO.postalCodeBilling(), billingCountry.getId());

        AddressEntity address = addressService.getOrCreateAddress(customerRequestDTO.addressLine(),
                city.getId(), addressCountry.getId());

        AddressEntity billingAddress = addressService.getOrCreateAddress(customerRequestDTO.addressLineBilling(),
                billingCity.getId(), billingCountry.getId());

        CustomerEntity customerEntity = new CustomerEntity(
                null,
                customerRequestDTO.firstName(),
                customerRequestDTO.lastName(),
                customerRequestDTO.email(),
                customerRequestDTO.phoneNumber(),
                customerRequestDTO.birthDate(),
                null,
                null,
                null,
                null,
                null,
                null,
                nationalityCountry.getId(),
                address.getId(),
                billingAddress.getId(),
                companyEntity.getId()
        );

        // ID Card processing
        if (hasIdCardData(customerRequestDTO, idCard)) {
            byte[] processedIdCard = fileService.processFile(idCard.getBytes());
            byte[] encryptedIdCard = encryptionService.encrypt(processedIdCard);
            String idCardUrl = fileService.uploadFileImage(encryptedIdCard);
            customerEntity.setIdCardCopyUrl(idCardUrl);
            customerEntity.setIdCardNumber(encryptionService.encrypt(customerRequestDTO.idCardNumber()));
            customerEntity.setIdCardExpirationDate(customerRequestDTO.idCardExpirationDate());
        }

        // Passport processing
        if (hasPassportData(customerRequestDTO, passport)) {
            byte[] processedPassport = fileService.processFile(passport.getBytes());
            byte[] encryptedPassport = encryptionService.encrypt(processedPassport);
            String passportUrl = fileService.uploadFileImage(encryptedPassport);
            customerEntity.setPassportCopyUrl(passportUrl);
            customerEntity.setPassportNumber(encryptionService.encrypt(customerRequestDTO.passportNumber()));
            customerEntity.setPassportExpirationDate(customerRequestDTO.passportExpirationDate());
        }

        customerService.addCustomer(customerEntity);
    }

    private void validateIsoCode(String code, String fieldName) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Le " + fieldName + " ne peut pas être null ou vide");
        }
    }

    private boolean hasIdCardData(CustomerRequestDTO dto, MultipartFile idCard) throws IOException {
        return Stream.of(
                dto.idCardNumber(),
                dto.idCardExpirationDate(),
                idCard != null ? idCard.getBytes() : null
        ).allMatch(Objects::nonNull);
    }

    private boolean hasPassportData(CustomerRequestDTO dto, MultipartFile passport) throws IOException {
        return Stream.of(
                dto.passportNumber(),
                dto.passportExpirationDate(),
                passport != null ? passport.getBytes() : null
        ).allMatch(Objects::nonNull);
    }



}
