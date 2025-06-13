package com.gosquad.presentation.controller.customers;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.domain.cities.CityEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.presentation.DTO.customers.GetCustomerResponseDTO;
import com.gosquad.usecase.addresses.AddressService;
import com.gosquad.usecase.cities.CityService;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.countries.CountryService;
import com.gosquad.usecase.customers.CustomerService;
import com.gosquad.usecase.files.FileService;
import com.gosquad.usecase.security.EncryptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.gosquad.presentation.DTO.customers.GetAllCustomersResponseDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Controller
public class CustomerGetController {

    private final CustomerService customerService;
    private final CompanyService companyService;
    private final JWTInterceptor jwtInterceptor;
    private final CountryService countryService;
    private final AddressService addressService;
    private final CityService cityService;
    private final EncryptionService encryptionService;
    private final FileService fileService;

    public CustomerGetController(CustomerService customerService, CompanyService companyService, JWTInterceptor jwtInterceptor, CountryService countryService, AddressService addressService, CityService cityService, EncryptionService encryptionService, FileService fileService) {
        this.customerService = customerService;
        this.companyService = companyService;
        this.jwtInterceptor = jwtInterceptor;
        this.countryService = countryService;
        this.addressService = addressService;
        this.cityService = cityService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<?> getAllCustomer(HttpServletRequest request){
        try{
            String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(7);
            Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);

            String companyCode = tokenInfo.get("companyCode").toString();

            CompanyEntity company = companyService.getCompanyByCode(companyCode);
            List<CustomerEntity> customers = customerService.getAllCustomers(company.getId());

            List<GetAllCustomersResponseDTO> responseList = customers.stream().map(customer -> {
                String uniqueId = companyCode + customer.getId();
                return new GetAllCustomersResponseDTO(
                        uniqueId,
                        customer.getFirstname(),
                        customer.getLastname(),
                        customer.getEmail()
                );
            }).toList();

            return ResponseEntity.ok(responseList);

        }catch(Exception e){
            return ResponseEntity.internalServerError()
                    .body("Database error: " + e.getMessage());
        }
    }

    @GetMapping("/getCustomer")
    public ResponseEntity<?> getCustomer(HttpServletRequest request) {
        try {
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

            GetCustomerResponseDTO response = new GetCustomerResponseDTO(
                    companyCode + customer.getId(),
                    customer.getFirstname(),
                    customer.getLastname(),
                    customer.getEmail(),
                    customer.getPhoneNumber(),
                    customer.getBirthDate(),
                    null,
                    null,
                    null,
                    null,
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
                    null,
                    null
            );

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
                response.setIdCardNumber(
                        encryptionService.decrypt(customer.getIdCardNumber())
                );
                response.setIdCardExpirationDate(
                        customer.getIdCardExpirationDate()
                );
                byte [] encryptedIdCard = fileService.downloadFileImage(customer.getIdCardCopyUrl());
                response.setIdCard(encryptionService.decrypt(encryptedIdCard));
            }

            if (hasPassportData) {
                response.setPassportNumber(
                        encryptionService.decrypt(customer.getPassportNumber())
                );
                response.setPassportExpirationDate(
                        customer.getPassportExpirationDate()
                );
                byte [] encryptedPassport = fileService.downloadFileImage(customer.getPassportCopyUrl());
                response.setPassport(encryptionService.decrypt(encryptedPassport));
            }
            return ResponseEntity.ok(response);


        }  catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Erreur base de données : " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Non trouvé : " + e.getMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Violation de contrainte : " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur d'entrée/sortie : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur inconnue : " + e.getMessage());
        }
    }
}
