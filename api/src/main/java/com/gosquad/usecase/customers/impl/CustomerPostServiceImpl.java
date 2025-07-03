package com.gosquad.usecase.customers.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.presentation.DTO.customers.CustomerRequestDTO;
import com.gosquad.usecase.customers.CustomerService;
import com.gosquad.usecase.customers.CustomerPostService;
import com.gosquad.usecase.customers.utils.CustomerValidationHelper;
import com.gosquad.usecase.files.FileService;
import com.gosquad.usecase.security.EncryptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class CustomerPostServiceImpl implements CustomerPostService {

    private final EncryptionService encryptionService;
    private final FileService fileService;
    private final CustomerService customerService;
    private final CustomerValidationHelper validationHelper;
    private final JWTInterceptor jwtInterceptor;
    public CustomerPostServiceImpl(EncryptionService encryptionService, FileService fileService,
                                   CustomerService customerService, CustomerValidationHelper validationHelper, JWTInterceptor jwtInterceptor) {
        this.encryptionService = encryptionService;
        this.fileService = fileService;
        this.customerService = customerService;
        this.validationHelper = validationHelper;
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public String createCustomer(HttpServletRequest request,CustomerRequestDTO customerRequestDTO, MultipartFile idCard, MultipartFile passport)
            throws Exception {

        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null ? authHeader.substring(7) : null;

        Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);
        String companyCode = tokenInfo.get("companyCode").toString();


        CustomerValidationHelper.ValidatedCustomerData validatedData =
                validationHelper.validateAndPrepareCustomerData(customerRequestDTO,companyCode);

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
                validatedData.getNationalityCountry().getId(),
                validatedData.getAddress().getId(),
                validatedData.getBillingAddress().getId(),
                validatedData.getCompany().getId()
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
        return companyCode + customerEntity.getId();
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
