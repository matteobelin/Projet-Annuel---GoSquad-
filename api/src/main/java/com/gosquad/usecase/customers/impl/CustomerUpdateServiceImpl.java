package com.gosquad.usecase.customers.impl;


import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.presentation.DTO.customers.CustomerRequestDTO;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.customers.CustomerService;
import com.gosquad.usecase.customers.CustomerUpdateService;
import com.gosquad.usecase.customers.utils.CustomerValidationHelper;
import com.gosquad.usecase.files.FileService;
import com.gosquad.usecase.security.EncryptionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class CustomerUpdateServiceImpl implements CustomerUpdateService {

    private final CustomerService customerService;
    private final CompanyService companyService;
    private final FileService fileService;
    private final EncryptionService encryptionService;
    private final CustomerValidationHelper validationHelper;

    public CustomerUpdateServiceImpl(CustomerService customerService,
                                     CompanyService companyService,
                                     FileService fileService, EncryptionService encryptionService, CustomerValidationHelper validationHelper) {

        this.customerService = customerService;
        this.companyService = companyService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
        this.validationHelper = validationHelper;
    }

    private String processAndUploadFile(MultipartFile file) throws Exception {
        byte[] processed = fileService.processFile(file.getBytes());
        byte[] encrypted = encryptionService.encrypt(processed);
        return fileService.uploadFileImage(encrypted);
    }

    @Override
    public void updateCustomer(CustomerRequestDTO customerRequestDTO, String companyCode) throws SQLException, NotFoundException, ConstraintViolationException {

        int customerId = Integer.parseInt(customerRequestDTO.uniqueCustomerId().replaceAll(companyCode, ""));

        CompanyEntity company = companyService.getCompanyByCode(companyCode);
        CustomerEntity existingCustomer = customerService.getCustomerByIdAndCompanyId(customerId, company.getId());

        CustomerValidationHelper.ValidatedCustomerData validatedData =
                validationHelper.validateAndPrepareCustomerData(customerRequestDTO, companyCode);


        CustomerEntity customerToUpdate = new CustomerEntity(
                customerId,
                customerRequestDTO.firstName(),
                customerRequestDTO.lastName(),
                customerRequestDTO.email(),
                customerRequestDTO.phoneNumber(),
                customerRequestDTO.birthDate(),
                existingCustomer.getIdCardNumber(),
                existingCustomer.getIdCardExpirationDate(),
                existingCustomer.getIdCardCopyUrl(),
                existingCustomer.getPassportNumber(),
                existingCustomer.getPassportExpirationDate(),
                existingCustomer.getPassportCopyUrl(),
                validatedData.getNationalityCountry().getId(),
                validatedData.getAddress().getId(),
                validatedData.getBillingAddress().getId(),
                validatedData.getCompany().getId()
        );

        customerService.updateCustomer(customerToUpdate);
    }

    @Override
    public void updateCustomerPassport(CustomerRequestDTO body, MultipartFile passport, String companyCode) throws Exception {
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
    public void updateCustomerIdCard(CustomerRequestDTO body, MultipartFile idCard, String companyCode) throws Exception {
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
    public void anonymizeCustomer(CustomerRequestDTO body, String companyCode) throws SQLException, NotFoundException, ConstraintViolationException {
        int customerId = Integer.parseInt(body.uniqueCustomerId().replaceAll(companyCode, ""));
        CompanyEntity company = companyService.getCompanyByCode(companyCode);
        CustomerEntity existingCustomer = customerService.getCustomerByIdAndCompanyId(customerId, company.getId());

        CustomerEntity anonymousCustomer = new CustomerEntity(
                customerId,
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

}
