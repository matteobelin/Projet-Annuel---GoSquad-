package com.gosquad.usecase.customers.impl;


import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.domain.cities.CityEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.presentation.DTO.customers.CustomerUpdateDTO;
import com.gosquad.usecase.addresses.AddressService;
import com.gosquad.usecase.cities.CityService;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.countries.CountryService;
import com.gosquad.usecase.customers.CustomerService;
import com.gosquad.usecase.customers.CustomerUpdateService;
import com.gosquad.usecase.files.FileService;
import com.gosquad.usecase.security.EncryptionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class CustomerUpdateServiceImpl implements CustomerUpdateService {

    private final CountryService countryService;
    private final AddressService addressService;
    private final CityService cityService;
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final FileService fileService;
    private final EncryptionService encryptionService;

    public CustomerUpdateServiceImpl(CountryService countryService,
                                     AddressService addressService,
                                     CityService cityService,
                                     CustomerService customerService,
                                     CompanyService companyService,
                                     FileService fileService, EncryptionService encryptionService) {
        this.countryService = countryService;
        this.addressService = addressService;
        this.cityService = cityService;
        this.customerService = customerService;
        this.companyService = companyService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
    }

    private String processAndUploadFile(MultipartFile file) throws Exception {
        byte[] processed = fileService.processFile(file.getBytes());
        byte[] encrypted = encryptionService.encrypt(processed);
        return fileService.uploadFileImage(encrypted);
    }

    @Override
    public void updateCustomer(CustomerUpdateDTO body, String companyCode) throws SQLException, NotFoundException, ConstraintViolationException {
        validateIsoCode(body.isoNationality(), "code ISO de nationalité");
        validateIsoCode(body.isoCode(), "code ISO d'adresse");
        validateIsoCode(body.isoCodeBilling(), "code ISO de facturation");

        int customerId = Integer.parseInt(body.uniqueCustomerId().replaceAll(companyCode, ""));
        CompanyEntity company = companyService.getCompanyByCode(companyCode);
        CustomerEntity existingCustomer = customerService.getCustomerByIdAndCompanyId(customerId, company.getId());

        CompanyEntity companyEntity = companyService.getCompanyByCode(body.companyCode());

        CountryEntity countryEntity = countryService.getCountryByIsoCode(body.isoNationality());
        CountryEntity countryEntityForAddress = countryService.getCountryByIsoCode(body.isoCode());
        CountryEntity countryEntityForAddressBilling = countryService.getCountryByIsoCode(body.isoCodeBilling());

        CityEntity cityEntity = cityService.getOrCreateCity(body.cityName(), body.postalCode(), countryEntityForAddress.getId());
        CityEntity cityEntityForBilling = cityService.getOrCreateCity(body.cityNameBilling(), body.postalCodeBilling(), countryEntityForAddressBilling.getId());

        AddressEntity addressEntity = addressService.getOrCreateAddress(body.addressLine(), cityEntity.getId(), countryEntityForAddress.getId());
        AddressEntity billingAddressEntity = addressService.getOrCreateAddress(body.addressLineBilling(), cityEntityForBilling.getId(), countryEntityForAddressBilling.getId());

        CustomerEntity customerToUpdate = new CustomerEntity(
                null,
                body.firstName(),
                body.lastName(),
                body.email(),
                body.phoneNumber(),
                body.birthDate(),
                existingCustomer.getIdCardNumber(),
                existingCustomer.getIdCardExpirationDate(),
                existingCustomer.getIdCardCopyUrl(),
                existingCustomer.getPassportNumber(),
                existingCustomer.getPassportExpirationDate(),
                existingCustomer.getPassportCopyUrl(),
                countryEntity.getId(),
                addressEntity.getId(),
                billingAddressEntity.getId(),
                companyEntity.getId()
        );

        customerService.updateCustomer(customerToUpdate);
    }

    @Override
    public void updateCustomerPassport(CustomerUpdateDTO body, MultipartFile passport, String companyCode) throws Exception {
        int customerId = Integer.parseInt(body.uniqueCustomerId().replaceAll(companyCode, ""));
        CompanyEntity company = companyService.getCompanyByCode(companyCode);
        CustomerEntity existingCustomer = customerService.getCustomerByIdAndCompanyId(customerId, company.getId());

        boolean hasIdCardData = Stream.of(
                passport,
                body.passportNumber(),
                body.passportExpirationDate()
        ).allMatch(Objects::nonNull);

        if(!hasIdCardData) {
            throw new IllegalArgumentException("Les données de la carte d'identité sont incomplètes");
        }
        if(existingCustomer.getPassportCopyUrl() != null) {
            fileService.deleteFileImage(existingCustomer.getPassportCopyUrl());
        }

        String passportUrl = processAndUploadFile(passport);

        CustomerEntity customerToUpdate = new CustomerEntity(
                existingCustomer.getId(),
                existingCustomer.getFirstname(),
                existingCustomer.getLastname(),
                existingCustomer.getEmail(),
                existingCustomer.getPhoneNumber(),
                existingCustomer.getBirthDate(),
                existingCustomer.getIdCardNumber(),
                existingCustomer.getIdCardExpirationDate(),
                existingCustomer.getIdCardCopyUrl(),
                encryptionService.encrypt(body.passportNumber()),
                body.passportExpirationDate(),
                passportUrl,
                existingCustomer.getCountryId(),
                existingCustomer.getAddressId(),
                existingCustomer.getBillingAddressId(),
                existingCustomer.getCompanyId()
        );

        customerService.updateCustomerPassport(customerToUpdate);
    }

    @Override
    public void updateCustomerIdCard(CustomerUpdateDTO body, MultipartFile idCard, String companyCode) throws Exception {
        int customerId = Integer.parseInt(body.uniqueCustomerId().replaceAll(companyCode, ""));
        CompanyEntity company = companyService.getCompanyByCode(companyCode);
        CustomerEntity existingCustomer = customerService.getCustomerByIdAndCompanyId(customerId, company.getId());

        boolean hasIdCardData = Stream.of(
                idCard,
                body.idCardNumber(),
                body.idCardExpirationDate()
        ).allMatch(Objects::nonNull);

        if(!hasIdCardData) {
            throw new IllegalArgumentException("Les données de la carte d'identité sont incomplètes");
        }
        if(existingCustomer.getIdCardCopyUrl() != null) {
            fileService.deleteFileImage(existingCustomer.getIdCardCopyUrl());
        }

        String idCardUrl = processAndUploadFile(idCard);

        CustomerEntity customerToUpdate = new CustomerEntity(
                existingCustomer.getId(),
                existingCustomer.getFirstname(),
                existingCustomer.getLastname(),
                existingCustomer.getEmail(),
                existingCustomer.getPhoneNumber(),
                existingCustomer.getBirthDate(),
                encryptionService.encrypt(body.idCardNumber()),
                body.idCardExpirationDate(),
                idCardUrl,
                existingCustomer.getPassportNumber(),
                existingCustomer.getPassportExpirationDate(),
                existingCustomer.getPassportCopyUrl(),
                existingCustomer.getCountryId(),
                existingCustomer.getAddressId(),
                existingCustomer.getBillingAddressId(),
                existingCustomer.getCompanyId()
        );

        customerService.updateCustomerIdCard(customerToUpdate);
    }

    @Override
    public void anonymizeCustomer(CustomerUpdateDTO body, String companyCode) throws SQLException, NotFoundException, ConstraintViolationException {
        int customerId = Integer.parseInt(body.uniqueCustomerId().replaceAll(companyCode, ""));
        CompanyEntity company = companyService.getCompanyByCode(companyCode);
        CustomerEntity existingCustomer = customerService.getCustomerByIdAndCompanyId(customerId, company.getId());

        CustomerEntity anonymousCustomer = new CustomerEntity(
                null,
                "anonymous",
                "anonymous",
                "anonymous@example.com",
                "0000000000",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                existingCustomer.getCountryId(),
                existingCustomer.getAddressId(),
                existingCustomer.getBillingAddressId(),
                existingCustomer.getCompanyId()
        );

        customerService.updateCustomer(anonymousCustomer);
    }

    private void validateIsoCode(String code, String fieldName) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Le " + fieldName + " ne peut pas être null ou vide");
        }
    }
}
