package com.gosquad.usecase.customers.impl;

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
import com.gosquad.usecase.customers.CustomerGetService;
import com.gosquad.usecase.customers.CustomerService;
import com.gosquad.usecase.files.FileService;
import com.gosquad.usecase.security.EncryptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;


@Service
public class CustomerGetServiceImpl implements CustomerGetService {

    private final EncryptionService encryptionService;
    private final FileService fileService;
    private final CountryService countryService;
    private final AddressService addressService;
    private final CityService cityService;
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final JWTInterceptor jwtInterceptor;

    public CustomerGetServiceImpl(EncryptionService encryptionService, FileService fileService, CountryService countryService, AddressService addressService, CityService cityService, CustomerService customerService, CompanyService companyService, JWTInterceptor jwtInterceptor) {
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
    public List<GetAllCustomersResponseDTO> getAllCustomers(HttpServletRequest request) throws Exception {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);

        String companyCode = tokenInfo.get("companyCode").toString();
        CompanyEntity company = companyService.getCompanyByCode(companyCode);

        List<CustomerEntity> customers = customerService.getAllCustomers(company.getId());

        return customers.stream()
                .filter(customer -> !"anonym".equalsIgnoreCase(customer.getFirstname()))
                .map(customer -> {
                    String uniqueId = companyCode + customer.getId();
                    return new GetAllCustomersResponseDTO(
                            uniqueId,
                            customer.getFirstname(),
                            customer.getLastname(),
                            customer.getEmail()
                    );
                })
                .toList();
    }

    @Override
    public GetCustomerResponseDTO getCustomer(HttpServletRequest request) throws Exception {

        String IdCardNumber = null;
        Date idCardExpirationDate = null;
        byte[] idCard = null;
        String passportNumber = null;
        Date passportExpirationDate = null;
        byte[] passport = null;

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);

        String companyCode = tokenInfo.get("companyCode").toString();
        String customerNumber = request.getParameter("customerNumber");

        int customerId = Integer.parseInt(customerNumber.replaceAll(companyCode, ""));
        CompanyEntity company = companyService.getCompanyByCode(companyCode);
        CustomerEntity customer = customerService.getCustomerByIdAndCompanyId(customerId, company.getId());

        CountryEntity countryNationality = countryService.getCountryById(customer.getCountryId());

        AddressEntity address = addressService.getAddressByID(customer.getAddressId());
        AddressEntity billingAddress = addressService.getAddressByID(customer.getBillingAddressId());

        CityEntity city = cityService.getCityById(address.getCityId());
        CityEntity billingCity = cityService.getCityById(billingAddress.getCityId());

        CountryEntity country = countryService.getCountryById(city.getCountryId());
        CountryEntity billingCountry = countryService.getCountryById(billingCity.getCountryId());


        boolean hasIdCardData = Stream.of(
                customer.getIdCardNumber(),
                customer.getIdCardExpirationDate(),
                customer.getIdCardCopyUrl()
        ).allMatch(Objects::nonNull);

        boolean hasPassportData = Stream.of(
                customer.getPassportNumber(),
                customer.getPassportExpirationDate(),
                customer.getPassportCopyUrl()
        ).allMatch(Objects::nonNull);

        if (hasIdCardData) {
            IdCardNumber = (encryptionService.decrypt(customer.getIdCardNumber()));
            idCardExpirationDate = (customer.getIdCardExpirationDate());
            byte[] encryptedIdCard = fileService.downloadFileImage(customer.getIdCardCopyUrl());
            idCard = (encryptionService.decrypt(encryptedIdCard));
        }

        if (hasPassportData) {
            passportNumber = (encryptionService.decrypt(customer.getPassportNumber()));
            passportExpirationDate = (customer.getPassportExpirationDate());
            byte[] encryptedPassport = fileService.downloadFileImage(customer.getPassportCopyUrl());
            passport = (encryptionService.decrypt(encryptedPassport));
        }

        return new GetCustomerResponseDTO(
                companyCode + customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getBirthDate(),
                IdCardNumber, idCardExpirationDate, passportNumber, passportExpirationDate,
                countryNationality.getIsoCode(),
                address.getAddressLine(),
                city.getCityName(),
                city.getPostalCode(),
                country.getIsoCode(),
                billingAddress.getAddressLine(),
                billingCity.getCityName(),
                billingCity.getPostalCode(),
                billingCountry.getIsoCode(),
                companyCode,
                idCard,
                passport
        );
    }
}
