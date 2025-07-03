package com.gosquad.usecase.customers;

import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.presentation.DTO.customers.CustomerRequestDTO;
import com.gosquad.usecase.customers.impl.CustomerPostServiceImpl;
import com.gosquad.usecase.customers.utils.CustomerValidationHelper;
import com.gosquad.usecase.files.FileService;
import com.gosquad.usecase.security.EncryptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerPostServiceImplTest {

    private EncryptionService encryptionService;
    private FileService fileService;
    private CustomerService customerService;
    private CustomerValidationHelper validationHelper;
    private JWTInterceptor jwtInterceptor;
    private HttpServletRequest request;

    private CustomerPostService customerPostService;

    @BeforeEach
    void setUp() {
        encryptionService = mock(EncryptionService.class);
        fileService = mock(FileService.class);
        customerService = mock(CustomerService.class);
        validationHelper = mock(CustomerValidationHelper.class);
        jwtInterceptor = mock(JWTInterceptor.class);
        request = mock(HttpServletRequest.class);

        customerPostService = new CustomerPostServiceImpl(encryptionService, fileService, customerService, validationHelper, jwtInterceptor);
    }

    @Test
    void createCustomer_shouldProcessAndSaveCustomer() throws Exception {
        // Préparation des données mock
        CustomerRequestDTO dto = mock(CustomerRequestDTO.class);
        MultipartFile idCardFile = mock(MultipartFile.class);
        MultipartFile passportFile = mock(MultipartFile.class);

        // Mock de la requête HTTP et du token
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken123");

        // Mock du token info
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("companyCode", "COMP001");
        when(jwtInterceptor.extractTokenInfo("mockToken123")).thenReturn(tokenInfo);

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
        when(validationHelper.validateAndPrepareCustomerData(dto, "COMP001")).thenReturn(validatedData);

        var nationalityCountry = mock(com.gosquad.domain.countries.CountryEntity.class);
        when(nationalityCountry.getId()).thenReturn(1);
        when(validatedData.getNationalityCountry()).thenReturn(nationalityCountry);

        var address = mock(com.gosquad.domain.addresses.AddressEntity.class);
        when(address.getId()).thenReturn(2);
        when(validatedData.getAddress()).thenReturn(address);

        var billingAddress = mock(com.gosquad.domain.addresses.AddressEntity.class);
        when(billingAddress.getId()).thenReturn(3);
        when(validatedData.getBillingAddress()).thenReturn(billingAddress);

        var company = mock(com.gosquad.domain.company.CompanyEntity.class);
        when(company.getId()).thenReturn(4);
        when(validatedData.getCompany()).thenReturn(company);

        // Simuler processFile et uploadFileImage
        when(fileService.processFile(idCardBytes)).thenReturn(idCardBytes);
        when(encryptionService.encrypt(idCardBytes)).thenReturn("encryptedIdCard".getBytes());
        when(fileService.uploadFileImage("encryptedIdCard".getBytes())).thenReturn("http://url/idCard");

        when(fileService.processFile(passportBytes)).thenReturn(passportBytes);
        when(encryptionService.encrypt(passportBytes)).thenReturn("encryptedPassport".getBytes());
        when(fileService.uploadFileImage("encryptedPassport".getBytes())).thenReturn("http://url/passport");

        // Simuler l'encryption des numéros (retourner des strings, pas des arrays)
        when(encryptionService.encrypt("ID123456")).thenReturn("encryptedIdNumber");
        when(encryptionService.encrypt("P1234567")).thenReturn("encryptedPassportNumber");

        // Mock pour que addCustomer fonctionne (simuler l'ID généré)
        doAnswer(invocation -> {
            CustomerEntity customer = invocation.getArgument(0);
            customer.setId(123); // Simuler l'ID généré
            return null;
        }).when(customerService).addCustomer(any(CustomerEntity.class));

        // Exécution de la méthode à tester
        String result = customerPostService.createCustomer(request, dto, idCardFile, passportFile);

        // Vérifications
        assertEquals("COMP001123", result);

        // Capture du CustomerEntity passé au service d'ajout
        ArgumentCaptor<CustomerEntity> captor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerService, times(1)).addCustomer(captor.capture());

        CustomerEntity savedCustomer = captor.getValue();

        // Vérifications des données du customer
        assertEquals("John", savedCustomer.getFirstname());
        assertEquals("Doe", savedCustomer.getLastname());
        assertEquals("john.doe@example.com", savedCustomer.getEmail());
        assertEquals("+1234567890", savedCustomer.getPhoneNumber());
        assertEquals(Date.valueOf("1980-01-01"), savedCustomer.getBirthDate());
        assertEquals("http://url/idCard", savedCustomer.getIdCardCopyUrl());
        assertEquals("http://url/passport", savedCustomer.getPassportCopyUrl());
        assertEquals("encryptedIdNumber", savedCustomer.getIdCardNumber());
        assertEquals("encryptedPassportNumber", savedCustomer.getPassportNumber());
        assertEquals(Date.valueOf("2030-01-01"), savedCustomer.getIdCardExpirationDate());
        assertEquals(Date.valueOf("2030-01-01"), savedCustomer.getPassportExpirationDate());
        assertEquals(Integer.valueOf(2), savedCustomer.getAddressId());
        assertEquals(Integer.valueOf(3), savedCustomer.getBillingAddressId());
        assertEquals(Integer.valueOf(4), savedCustomer.getCompanyId());

        // Vérifications des interactions
        verify(jwtInterceptor).extractTokenInfo("mockToken123");
        verify(validationHelper).validateAndPrepareCustomerData(dto, "COMP001");
        verify(fileService).processFile(idCardBytes);
        verify(fileService).processFile(passportBytes);
        verify(encryptionService).encrypt(idCardBytes);
        verify(encryptionService).encrypt(passportBytes);
        verify(encryptionService).encrypt("ID123456");
        verify(encryptionService).encrypt("P1234567");
        verify(fileService).uploadFileImage("encryptedIdCard".getBytes());
        verify(fileService).uploadFileImage("encryptedPassport".getBytes());
    }

    @Test
    void createCustomer_shouldWorkWithoutIdCard() throws Exception {
        // Test avec seulement les données passport
        CustomerRequestDTO dto = mock(CustomerRequestDTO.class);
        MultipartFile passportFile = mock(MultipartFile.class);

        // Mock de la requête HTTP et du token
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken123");

        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("companyCode", "COMP001");
        when(jwtInterceptor.extractTokenInfo("mockToken123")).thenReturn(tokenInfo);

        // Données de base
        when(dto.firstName()).thenReturn("Jane");
        when(dto.lastName()).thenReturn("Smith");
        when(dto.email()).thenReturn("jane.smith@example.com");
        when(dto.phoneNumber()).thenReturn("+1234567890");
        when(dto.birthDate()).thenReturn(Date.valueOf("1990-01-01"));

        // Pas de données ID Card
        when(dto.idCardNumber()).thenReturn(null);
        when(dto.idCardExpirationDate()).thenReturn(null);

        // Données passport
        when(dto.passportNumber()).thenReturn("P7654321");
        when(dto.passportExpirationDate()).thenReturn(Date.valueOf("2030-01-01"));
        byte[] passportBytes = "passportBytes".getBytes();
        when(passportFile.getBytes()).thenReturn(passportBytes);

        // Mock des entités de validation
        var validatedData = mock(CustomerValidationHelper.ValidatedCustomerData.class);
        when(validationHelper.validateAndPrepareCustomerData(dto, "COMP001")).thenReturn(validatedData);

        var nationalityCountry = mock(com.gosquad.domain.countries.CountryEntity.class);
        when(nationalityCountry.getId()).thenReturn(1);
        when(validatedData.getNationalityCountry()).thenReturn(nationalityCountry);

        var address = mock(com.gosquad.domain.addresses.AddressEntity.class);
        when(address.getId()).thenReturn(2);
        when(validatedData.getAddress()).thenReturn(address);

        var billingAddress = mock(com.gosquad.domain.addresses.AddressEntity.class);
        when(billingAddress.getId()).thenReturn(3);
        when(validatedData.getBillingAddress()).thenReturn(billingAddress);

        var company = mock(com.gosquad.domain.company.CompanyEntity.class);
        when(company.getId()).thenReturn(4);
        when(validatedData.getCompany()).thenReturn(company);

        // Mock pour passport seulement
        when(fileService.processFile(passportBytes)).thenReturn(passportBytes);
        when(encryptionService.encrypt(passportBytes)).thenReturn("encryptedPassport".getBytes());
        when(fileService.uploadFileImage("encryptedPassport".getBytes())).thenReturn("http://url/passport");
        when(encryptionService.encrypt("P7654321")).thenReturn("encryptedPassportNumber");

        // Mock pour addCustomer
        doAnswer(invocation -> {
            CustomerEntity customer = invocation.getArgument(0);
            customer.setId(456);
            return null;
        }).when(customerService).addCustomer(any(CustomerEntity.class));

        // Exécution
        String result = customerPostService.createCustomer(request, dto, null, passportFile);

        // Vérifications
        assertEquals("COMP001456", result);

        ArgumentCaptor<CustomerEntity> captor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerService).addCustomer(captor.capture());

        CustomerEntity savedCustomer = captor.getValue();

        // Vérifier que les données ID Card sont nulles
        assertNull(savedCustomer.getIdCardCopyUrl());
        assertNull(savedCustomer.getIdCardNumber());
        assertNull(savedCustomer.getIdCardExpirationDate());

        // Vérifier que les données passport sont présentes
        assertEquals("http://url/passport", savedCustomer.getPassportCopyUrl());
        assertEquals("encryptedPassportNumber", savedCustomer.getPassportNumber());
        assertEquals(Date.valueOf("2030-01-01"), savedCustomer.getPassportExpirationDate());
    }
}