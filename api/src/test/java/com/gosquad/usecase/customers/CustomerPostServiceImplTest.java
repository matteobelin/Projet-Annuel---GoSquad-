package com.gosquad.usecase.customers;

import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.presentation.DTO.customers.CustomerRequestDTO;
import com.gosquad.usecase.customers.impl.CustomerPostServiceImpl;
import com.gosquad.usecase.customers.utils.CustomerValidationHelper;
import com.gosquad.usecase.files.FileService;
import com.gosquad.usecase.security.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Date;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

public class CustomerPostServiceImplTest {

    private EncryptionService encryptionService;
    private FileService fileService;
    private CustomerService customerService;
    private CustomerValidationHelper validationHelper;

    private CustomerPostService customerPostService;

    @BeforeEach
    void setUp() {
        encryptionService = mock(EncryptionService.class);
        fileService = mock(FileService.class);
        customerService = mock(CustomerService.class);
        validationHelper = mock(CustomerValidationHelper.class);

        customerPostService = new CustomerPostServiceImpl(encryptionService, fileService, customerService, validationHelper);
    }

    @Test
    void createCustomer_shouldProcessAndSaveCustomer() throws Exception {
        // Préparation des données mock
        CustomerRequestDTO dto = mock(CustomerRequestDTO.class);
        MultipartFile idCardFile = mock(MultipartFile.class);
        MultipartFile passportFile = mock(MultipartFile.class);

        // Données retournées par le DTO
        when(dto.firstName()).thenReturn("John");
        when(dto.lastName()).thenReturn("Doe");
        when(dto.email()).thenReturn("john.doe@example.com");
        when(dto.phoneNumber()).thenReturn("+1234567890");
        when(dto.birthDate()).thenReturn(Date.valueOf("1980-01-01"));

        // Simuler les données ID Card
        when(dto.idCardNumber()).thenReturn("ID123456");
        when(dto.idCardExpirationDate()).thenReturn(Date.valueOf("2030-01-01"));
        byte[] idCardBytes = "idCardBytes".getBytes();
        when(idCardFile.getBytes()).thenReturn(idCardBytes);

        // Simuler les données Passport
        when(dto.passportNumber()).thenReturn("P1234567");
        when(dto.passportExpirationDate()).thenReturn(Date.valueOf("2030-01-01"));
        byte[] passportBytes = "passportBytes".getBytes();
        when(passportFile.getBytes()).thenReturn(passportBytes);

        // Simuler validation helper (retourner des entités nécessaires)
        var validatedData = mock(CustomerValidationHelper.ValidatedCustomerData.class);
        when(validationHelper.validateAndPrepareCustomerData(dto)).thenReturn(validatedData);
        when(validatedData.getNationalityCountry()).thenReturn(mock(com.gosquad.domain.countries.CountryEntity.class));
        when(validatedData.getNationalityCountry().getId()).thenReturn(1);
        when(validatedData.getAddress()).thenReturn(mock(com.gosquad.domain.addresses.AddressEntity.class));
        when(validatedData.getAddress().getId()).thenReturn(2);
        when(validatedData.getBillingAddress()).thenReturn(mock(com.gosquad.domain.addresses.AddressEntity.class));
        when(validatedData.getBillingAddress().getId()).thenReturn(3);
        when(validatedData.getCompany()).thenReturn(mock(com.gosquad.domain.company.CompanyEntity.class));
        when(validatedData.getCompany().getId()).thenReturn(4);

        // Simuler processFile et uploadFileImage
        when(fileService.processFile(idCardBytes)).thenReturn(idCardBytes);
        when(encryptionService.encrypt(idCardBytes)).thenReturn("encryptedIdCard".getBytes());
        when(fileService.uploadFileImage("encryptedIdCard".getBytes())).thenReturn("http://url/idCard");

        when(fileService.processFile(passportBytes)).thenReturn(passportBytes);
        when(encryptionService.encrypt(passportBytes)).thenReturn("encryptedPassport".getBytes());
        when(fileService.uploadFileImage("encryptedPassport".getBytes())).thenReturn("http://url/passport");

        // Simuler l'encryption des numéros
        when(encryptionService.encrypt("ID123456")).thenReturn(Arrays.toString("encryptedIdNumber".getBytes()));
        when(encryptionService.encrypt("P1234567")).thenReturn(Arrays.toString("encryptedPassportNumber".getBytes()));

        // Exécution de la méthode à tester
        customerPostService.createCustomer(dto, idCardFile, passportFile);

        // Capture du CustomerEntity passé au service d’ajout
        ArgumentCaptor<CustomerEntity> captor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerService, times(1)).addCustomer(captor.capture());

        CustomerEntity savedCustomer = captor.getValue();

        // Vérifications simples
        assertEquals("John", savedCustomer.getFirstname());
        assertEquals("Doe", savedCustomer.getLastname());
        assertEquals("http://url/idCard", savedCustomer.getIdCardCopyUrl());
        assertEquals("http://url/passport", savedCustomer.getPassportCopyUrl());
    }
}

