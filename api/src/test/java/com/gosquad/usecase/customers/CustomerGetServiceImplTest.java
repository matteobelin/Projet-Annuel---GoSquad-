package com.gosquad.usecase.customers;

import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.domain.cities.CityEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.presentation.DTO.customers.GetAllCustomersResponseDTO;
import com.gosquad.presentation.DTO.customers.GetCustomerResponseDTO;
import com.gosquad.usecase.addresses.AddressService;
import com.gosquad.usecase.cities.CityService;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.countries.CountryService;
import com.gosquad.usecase.customers.impl.CustomerGetServiceImpl;
import com.gosquad.usecase.files.FileService;
import com.gosquad.usecase.security.EncryptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerGetServiceImplTest {

    private EncryptionService encryptionService;
    private FileService fileService;
    private CountryService countryService;
    private AddressService addressService;
    private CityService cityService;
    private CustomerService customerService;
    private CompanyService companyService;
    private JWTInterceptor jwtInterceptor;

    private CustomerGetServiceImpl customerGetService;

    @BeforeEach
    void setUp() {
        encryptionService = mock(EncryptionService.class);
        fileService = mock(FileService.class);
        countryService = mock(CountryService.class);
        addressService = mock(AddressService.class);
        cityService = mock(CityService.class);
        customerService = mock(CustomerService.class);
        companyService = mock(CompanyService.class);
        jwtInterceptor = mock(JWTInterceptor.class);

        customerGetService = new CustomerGetServiceImpl(
                encryptionService, fileService, countryService, addressService,
                cityService, customerService, companyService, jwtInterceptor);
    }

    @Test
    void testGetAllCustomers() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtInterceptor.extractTokenInfo("token123")).thenReturn(Map.of("companyCode", "COMP"));

        CompanyEntity company = new CompanyEntity(1, "COMP", "Company Name");
        when(companyService.getCompanyByCode("COMP")).thenReturn(company);

        CustomerEntity c1 = new CustomerEntity(1, "John", "Doe", "john@example.com", "+33612345678", null, null, null, null, null, null, null, 1, 1, 1, 1);
        CustomerEntity c2 = new CustomerEntity(2, "Anonym", "User", "anon@example.com", "+33612345678", null, null, null, null, null, null, null, 1, 1, 1, 1);

        when(customerService.getAllCustomers(company.getId())).thenReturn(List.of(c1, c2));

        List<GetAllCustomersResponseDTO> result = customerGetService.getAllCustomers(request);

        assertEquals(2, result.size());
        GetAllCustomersResponseDTO dto = result.get(0);
        assertEquals("COMP1", dto.uniqueCustomerId());
        assertEquals("John", dto.firstName());
        assertEquals("Doe", dto.lastName());
        assertEquals("john@example.com", dto.email());
    }

    @Test
    void testGetCustomer_withIdCardAndPassport() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(request.getParameter("customerNumber")).thenReturn("COMP1");

        when(jwtInterceptor.extractTokenInfo("token123")).thenReturn(Map.of("companyCode", "COMP"));

        CompanyEntity company = new CompanyEntity(1, "COMP", "Company Name");
        when(companyService.getCompanyByCode("COMP")).thenReturn(company);

        CustomerEntity customer = new CustomerEntity(
                1, "John", "Doe", "john@example.com", "1234567890",
                new Date(System.currentTimeMillis()),
                "encryptedIdCardNumber", new Date(System.currentTimeMillis() + 10000), "idCardUrl",
                "encryptedPassportNumber", new Date(System.currentTimeMillis() + 20000), "passportUrl",
                1, 1, 2, 3);
        when(customerService.getCustomerByIdAndCompanyId(1, 1)).thenReturn(customer);

        CountryEntity countryNationality = new CountryEntity(1, "FR", "France");
        when(countryService.getCountryById(1)).thenReturn(countryNationality);

        AddressEntity address = new AddressEntity(1, "Address Line 1", 10);
        AddressEntity billingAddress = new AddressEntity(2, "Billing Address", 20);
        when(addressService.getAddressByID(1)).thenReturn(address);
        when(addressService.getAddressByID(2)).thenReturn(billingAddress);

        CityEntity city = new CityEntity(10, "Paris", "75000", 1);
        CityEntity billingCity = new CityEntity(20, "Lyon", "69000", 1);
        when(cityService.getCityById(10)).thenReturn(city);
        when(cityService.getCityById(20)).thenReturn(billingCity);

        CountryEntity country = new CountryEntity(1, "FR", "France");
        CountryEntity billingCountry = new CountryEntity(1, "FR", "France");
        when(countryService.getCountryById(1)).thenReturn(country);
        when(countryService.getCountryById(1)).thenReturn(billingCountry);

        when(encryptionService.decrypt("encryptedIdCardNumber")).thenReturn("123456789");
        when(encryptionService.decrypt("encryptedPassportNumber")).thenReturn("987654321");

        byte[] encryptedIdCardFile = new byte[]{1, 2, 3};
        byte[] decryptedIdCardFile = new byte[]{4, 5, 6};
        when(fileService.downloadFileImage("idCardUrl")).thenReturn(encryptedIdCardFile);
        when(encryptionService.decrypt(encryptedIdCardFile)).thenReturn(decryptedIdCardFile);

        byte[] encryptedPassportFile = new byte[]{7, 8, 9};
        byte[] decryptedPassportFile = new byte[]{10, 11, 12};
        when(fileService.downloadFileImage("passportUrl")).thenReturn(encryptedPassportFile);
        when(encryptionService.decrypt(encryptedPassportFile)).thenReturn(decryptedPassportFile);

        GetCustomerResponseDTO dto = customerGetService.getCustomer(request);

        assertEquals("COMP1", dto.uniqueCustomerId());
        assertEquals("John", dto.firstName());
        assertEquals("Doe", dto.lastName());
        assertEquals("john@example.com", dto.email());
        assertEquals("123456789", dto.idCardNumber());
        assertArrayEquals(decryptedIdCardFile, dto.idCard());
        assertEquals("987654321", dto.passportNumber());
        assertArrayEquals(decryptedPassportFile, dto.passport());
    }
}
