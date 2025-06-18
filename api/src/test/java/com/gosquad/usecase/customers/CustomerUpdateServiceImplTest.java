package com.gosquad.usecase.customers;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.presentation.DTO.customers.CustomerRequestDTO;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.customers.impl.CustomerUpdateServiceImpl;
import com.gosquad.usecase.customers.utils.CustomerValidationHelper;
import com.gosquad.usecase.files.FileService;
import com.gosquad.usecase.security.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerUpdateServiceImplTest {

    private CustomerService customerService;
    private CompanyService companyService;
    private FileService fileService;
    private EncryptionService encryptionService;
    private CustomerValidationHelper validationHelper;

    private CustomerUpdateServiceImpl updateService;

    @BeforeEach
    void setup() {
        customerService = mock(CustomerService.class);
        companyService = mock(CompanyService.class);
        fileService = mock(FileService.class);
        encryptionService = mock(EncryptionService.class);
        validationHelper = mock(CustomerValidationHelper.class);

        updateService = new CustomerUpdateServiceImpl(customerService, companyService, fileService, encryptionService, validationHelper);
    }

    @Test
    void testUpdateCustomer_success() throws Exception {
        String companyCode = "COMP";
        String uniqueCustomerId = "COMP123";
        int customerId = 123;

        CustomerRequestDTO dto = mock(CustomerRequestDTO.class);
        when(dto.uniqueCustomerId()).thenReturn(uniqueCustomerId);
        when(dto.firstName()).thenReturn("John");
        when(dto.lastName()).thenReturn("Doe");
        when(dto.email()).thenReturn("john@example.com");
        when(dto.phoneNumber()).thenReturn("+123456789");
        when(dto.birthDate()).thenReturn(Date.valueOf("1980-01-01"));

        CompanyEntity company = mock(CompanyEntity.class);
        when(company.getId()).thenReturn(10);
        when(companyService.getCompanyByCode(companyCode)).thenReturn(company);

        CustomerEntity existingCustomer = new CustomerEntity(
                customerId,
                "OldFirstName",
                "OldLastName",
                "oldemail@example.com",
                "+33612345678",
                Date.valueOf("1970-01-01"),
                null, null, null, null, null, null,
                1, 2, 3, 10);

        when(customerService.getCustomerByIdAndCompanyId(customerId, company.getId())).thenReturn(existingCustomer);

        var validatedData = mock(CustomerValidationHelper.ValidatedCustomerData.class);
        when(validationHelper.validateAndPrepareCustomerData(dto)).thenReturn(validatedData);
        when(validatedData.getNationalityCountry()).thenReturn(mock(com.gosquad.domain.countries.CountryEntity.class));
        when(validatedData.getNationalityCountry().getId()).thenReturn(100);
        when(validatedData.getAddress()).thenReturn(mock(com.gosquad.domain.addresses.AddressEntity.class));
        when(validatedData.getAddress().getId()).thenReturn(200);
        when(validatedData.getBillingAddress()).thenReturn(mock(com.gosquad.domain.addresses.AddressEntity.class));
        when(validatedData.getBillingAddress().getId()).thenReturn(300);
        when(validatedData.getCompany()).thenReturn(company);

        updateService.updateCustomer(dto, companyCode);

        ArgumentCaptor<CustomerEntity> captor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerService).updateCustomer(captor.capture());

        CustomerEntity updated = captor.getValue();
        assertEquals("John", updated.getFirstname());
        assertEquals("Doe", updated.getLastname());
        assertEquals(100, updated.getCountryId());
        assertEquals(200, updated.getAddressId());
        assertEquals(300, updated.getBillingAddressId());
        assertEquals(10, updated.getCompanyId());
    }

    @Test
    void testUpdateCustomerPassport_success() throws Exception {
        String companyCode = "COMP";
        String uniqueCustomerId = "COMP123";
        int customerId = 123;

        CustomerRequestDTO dto = mock(CustomerRequestDTO.class);
        when(dto.uniqueCustomerId()).thenReturn(uniqueCustomerId);
        when(dto.passportNumber()).thenReturn("P123456");
        when(dto.passportExpirationDate()).thenReturn(Date.valueOf("2030-01-01"));

        MultipartFile passportFile = mock(MultipartFile.class);
        byte[] passportBytes = "passportBytes".getBytes();

        when(passportFile.getBytes()).thenReturn(passportBytes);

        CompanyEntity company = mock(CompanyEntity.class);
        when(company.getId()).thenReturn(10);
        when(companyService.getCompanyByCode(companyCode)).thenReturn(company);

        CustomerEntity existingCustomer = new CustomerEntity(
                customerId, "First", "Last", "email@example.com", "+33612345678", Date.valueOf("1980-01-01"),
                null, null, null, "oldPassportNum", Date.valueOf("2025-01-01"), "oldPassportUrl",
                1, 2, 3, 10);
        when(customerService.getCustomerByIdAndCompanyId(customerId, company.getId())).thenReturn(existingCustomer);

        when(fileService.processFile(passportBytes)).thenReturn(passportBytes);
        when(encryptionService.encrypt(passportBytes)).thenReturn("encryptedPassport".getBytes());
        when(fileService.uploadFileImage("encryptedPassport".getBytes())).thenReturn("newPassportUrl");
        when(encryptionService.encrypt("P123456")).thenReturn("encryptedPassportNum");

        updateService.updateCustomerPassport(dto, passportFile, companyCode);

        verify(fileService).deleteFileImage("oldPassportUrl");

        ArgumentCaptor<CustomerEntity> captor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerService).updateCustomerPassport(captor.capture());

        CustomerEntity updated = captor.getValue();
        assertEquals("encryptedPassportNum", updated.getPassportNumber());
        assertEquals("newPassportUrl", updated.getPassportCopyUrl());
    }

    @Test
    void testUpdateCustomerPassport_missingData_throws() throws SQLException, NotFoundException {
        CustomerRequestDTO dto = mock(CustomerRequestDTO.class);
        MultipartFile passportFile = null;
        when(dto.passportNumber()).thenReturn(null);
        when(dto.passportExpirationDate()).thenReturn(null);
        when(dto.uniqueCustomerId()).thenReturn("COMP123");

        // Mock de companyService pour éviter NullPointerException
        CompanyEntity company = mock(CompanyEntity.class);
        when(company.getId()).thenReturn(1);
        when(companyService.getCompanyByCode("COMP")).thenReturn(company);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            updateService.updateCustomerPassport(dto, passportFile, "COMP");
        });

        assertEquals("Les données de la carte d'identité sont incomplètes", exception.getMessage());
    }


    @Test
    void testUpdateCustomerIdCard_success() throws Exception {
        String companyCode = "COMP";
        String uniqueCustomerId = "COMP123";
        int customerId = 123;

        CustomerRequestDTO dto = mock(CustomerRequestDTO.class);
        when(dto.uniqueCustomerId()).thenReturn(uniqueCustomerId);
        when(dto.idCardNumber()).thenReturn("ID123456");
        when(dto.idCardExpirationDate()).thenReturn(Date.valueOf("2030-01-01"));

        MultipartFile idCardFile = mock(MultipartFile.class);
        byte[] idCardBytes = "idCardBytes".getBytes();

        when(idCardFile.getBytes()).thenReturn(idCardBytes);

        CompanyEntity company = mock(CompanyEntity.class);
        when(company.getId()).thenReturn(10);
        when(companyService.getCompanyByCode(companyCode)).thenReturn(company);

        CustomerEntity existingCustomer = new CustomerEntity(
                customerId, "First", "Last", "email@example.com", "+33612345678", Date.valueOf("1980-01-01"),
                "oldIdCardNum", Date.valueOf("2025-01-01"), "oldIdCardUrl", null, null, null,
                1, 2, 3, 10);
        when(customerService.getCustomerByIdAndCompanyId(customerId, company.getId())).thenReturn(existingCustomer);

        when(fileService.processFile(idCardBytes)).thenReturn(idCardBytes);
        when(encryptionService.encrypt(idCardBytes)).thenReturn("encryptedIdCard".getBytes());
        when(fileService.uploadFileImage("encryptedIdCard".getBytes())).thenReturn("newIdCardUrl");
        when(encryptionService.encrypt("ID123456")).thenReturn("encryptedIdCardNum");

        updateService.updateCustomerIdCard(dto, idCardFile, companyCode);

        verify(fileService).deleteFileImage("oldIdCardUrl");

        ArgumentCaptor<CustomerEntity> captor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerService).updateCustomerIdCard(captor.capture());

        CustomerEntity updated = captor.getValue();
        assertEquals("encryptedIdCardNum", updated.getIdCardNumber());
        assertEquals("newIdCardUrl", updated.getIdCardCopyUrl());
    }

    @Test
    void testUpdateCustomerIdCard_missingData_throws() throws SQLException, NotFoundException {
        CustomerRequestDTO dto = mock(CustomerRequestDTO.class);
        MultipartFile idCardFile = null;
        when(dto.idCardNumber()).thenReturn(null);
        when(dto.idCardExpirationDate()).thenReturn(null);
        when(dto.uniqueCustomerId()).thenReturn("COMP123");

        // Mock de companyService pour éviter NullPointerException
        CompanyEntity company = mock(CompanyEntity.class);
        when(company.getId()).thenReturn(1);
        when(companyService.getCompanyByCode("COMP")).thenReturn(company);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            updateService.updateCustomerIdCard(dto, idCardFile, "COMP");
        });

        assertEquals("Les données de la carte d'identité sont incomplètes", exception.getMessage());
    }


    @Test
    void testAnonymizeCustomer_success() throws SQLException, NotFoundException, ConstraintViolationException {
        String companyCode = "COMP";
        String uniqueCustomerId = "COMP123";
        int customerId = 123;

        CustomerRequestDTO dto = mock(CustomerRequestDTO.class);
        when(dto.uniqueCustomerId()).thenReturn(uniqueCustomerId);

        CompanyEntity company = mock(CompanyEntity.class);
        when(company.getId()).thenReturn(10);
        when(companyService.getCompanyByCode(companyCode)).thenReturn(company);

        CustomerEntity existingCustomer = new CustomerEntity(
                customerId, "John", "Doe", "john@example.com", "+123456789",
                java.sql.Date.valueOf("1980-01-01"), null, null, null, null, null, null,
                1, 2, 3, 10);

        when(customerService.getCustomerByIdAndCompanyId(customerId, company.getId())).thenReturn(existingCustomer);

        updateService.anonymizeCustomer(dto, companyCode);

        ArgumentCaptor<CustomerEntity> captor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerService).updateCustomer(captor.capture());

        CustomerEntity anonymized = captor.getValue();

        assertEquals("anonymous", anonymized.getFirstname());
        assertEquals("anonymous", anonymized.getLastname());
        assertEquals("anonymous@example.com", anonymized.getEmail());
        assertEquals("0000000000", anonymized.getPhoneNumber());
        assertNull(anonymized.getBirthDate());
        // Vérifie que les IDs restent les mêmes (lié au company, addresses, etc)
        assertEquals(existingCustomer.getCountryId(), anonymized.getCountryId());
        assertEquals(existingCustomer.getAddressId(), anonymized.getAddressId());
        assertEquals(existingCustomer.getBillingAddressId(), anonymized.getBillingAddressId());
        assertEquals(existingCustomer.getCompanyId(), anonymized.getCompanyId());
    }
}

